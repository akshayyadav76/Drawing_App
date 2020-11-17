package com.ak.drawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.brush_size.*

class MainActivity : AppCompatActivity() {

    private var mImageButtonCurrentPoint:Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DrowingView.setSizeForBrus(20.toFloat())
        mImageButtonCurrentPoint = ll_point_colors[1] as Button
        val drawble = ContextCompat.getDrawable(this,R.drawable.selected)
        mImageButtonCurrentPoint!!.setCompoundDrawablesWithIntrinsicBounds(drawble,null,null,null)

        id_brush.setOnClickListener {
            showBrushSize()
        }

    }

    private fun showBrushSize(){
        val brushDilog = Dialog(this)
        brushDilog.setContentView(R.layout.brush_size)
        brushDilog.setTitle("Brush Size")

        val small =brushDilog.small_button
        small.setOnClickListener{
            DrowingView.setSizeForBrus(10.toFloat())
            brushDilog.dismiss()
        }
        brushDilog.show()

    }

    fun paintCliked(view: View){
        if(view != mImageButtonCurrentPoint){
            val button = view as Button
            val colorTag = button.tag.toString()
            DrowingView.setColor(colorTag)
            val drawble = ContextCompat.getDrawable(this,R.drawable.selected)
            button!!.setCompoundDrawablesWithIntrinsicBounds(drawble,null,null,null)

            val drawbl2e = ContextCompat.getDrawable(this,R.drawable.selected)
            mImageButtonCurrentPoint!!.setCompoundDrawablesWithIntrinsicBounds(drawble,null,null,null)
            mImageButtonCurrentPoint =view
        }

    }
}