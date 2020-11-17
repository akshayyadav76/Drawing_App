package com.ak.drawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import java.nio.file.Paths


class DrawingView(context: Context, attrs: AttributeSet) : View(context,attrs) {

    private var mDrowPath : CustomPath?=null
    private var mCanvasBitmap:Bitmap?=null
    private var mDrawPaint: Paint?=null
    private var mCanvasPaint:Paint?=null
    private var mBrushSize :Float =0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas?=null
    private var mPaths =ArrayList<CustomPath>()

    init {
        setUpDrawing()
    }

    private fun setUpDrawing(){
        mDrawPaint = Paint()
        mDrowPath =CustomPath(color,mBrushSize)

        mDrawPaint!!.color =color
        mDrawPaint!!.style =Paint.Style.STROKE
        mDrawPaint!!.strokeJoin =Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
            // mBrushSize =20.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!,0f,0f,mCanvasPaint)

        for(path in mPaths){
            mDrawPaint!!.strokeWidth =path.brushThikness
            mDrawPaint!!.color =path.color
            canvas.drawPath(path,mDrawPaint!!)
        }



        if(!mDrowPath!!.isEmpty){
            mDrawPaint!!.strokeWidth =mDrowPath!!.brushThikness
            mDrawPaint!!.color =mDrowPath!!.color
            canvas.drawPath(mDrowPath!!,mDrawPaint!!)


        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        val touchX =event?.x
        val touchy = event?.y

        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                mDrowPath!!.color = color
                mDrowPath!!.brushThikness = mBrushSize

                mDrowPath!!.reset()
                mDrowPath!!.moveTo(touchX!!,touchy!!)

            }
            MotionEvent.ACTION_MOVE->{
                mDrowPath!!.lineTo(touchX!!,touchy!!)
            }
            MotionEvent.ACTION_UP->{
                mPaths.add(mDrowPath!!)
                mDrowPath =CustomPath(color,mBrushSize)
            }
            else->return false

        }
        invalidate()
        return true
    }

    fun setSizeForBrus(newSize:Float){
        mBrushSize =TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,newSize,resources.displayMetrics)
        mDrawPaint!!.strokeWidth = mBrushSize
    }


 internal inner class CustomPath(var color:Int,var brushThikness:Float): Path(){

    }
}

