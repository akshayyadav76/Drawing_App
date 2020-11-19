package com.ak.drawingapp

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.brush_size.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


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

        //=================pick gallery
        id_gallery.setOnClickListener {
            if(isReadStorageAlloed()){
                val pickPhotoIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhotoIntent, Gallery)

            }else{
                requestStoragePersmission()
            }
        }


        id_Undo.setOnClickListener {
            DrowingView.onClickUndo()
        }

        id_Save.setOnClickListener {
            if(isReadStorageAlloed()){
                BitmapAsyncTask(getBitmapFromView(frame)).execute()
            }else{
                requestStoragePersmission()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Gallery){
                try {
                    if(data!!.data !=null){
                        iv_backgroud.visibility = View.VISIBLE
                        iv_backgroud.setImageURI(data.data)
                    }else{
                        Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
                    }
                }catch (e:Exception){e.printStackTrace()}
            }
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

    private fun requestStoragePersmission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE).toString())){
            Toast.makeText(this,"need permission to add",Toast.LENGTH_SHORT).show()

        }
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),
            Storage_Premission_Code)
    }

    private fun isReadStorageAlloed():Boolean{
        val result = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Storage_Premission_Code){
            if(grantResults.isNotEmpty()&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"permission granted",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"no permission",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getBitmapFromView(view: View):Bitmap{
        val retunBitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)

        val cancvas = Canvas(retunBitmap)
        val bgDrawble =view.background
        if(bgDrawble !=null){
            bgDrawble.draw(cancvas)
        }else{
            cancvas.drawColor(Color.WHITE)
        }
        view.draw(cancvas)
        return  retunBitmap
    }

    private inner class BitmapAsyncTask(val mBitmap:Bitmap):AsyncTask<Any,Void,String>(){



        override fun doInBackground(vararg p0: Any?): String {
            var reuslt = ""
            if(mBitmap !=null){
                try {
                    val bytes = ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.PNG,90,bytes)
                    val file = File(externalCacheDir!!.absoluteFile.toString()+
                            File.separator + "KidesDrowing" + System.currentTimeMillis()/1000 +".png")

                    val fos = FileOutputStream(file)
                    fos.write(bytes.toByteArray())
                    fos.close()
                    reuslt = file.absolutePath
                }catch (e:Exception){
                    reuslt =""
                    e.printStackTrace()
                }
            }
            return  reuslt
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(!result!!.isEmpty()){
                Toast.makeText(this@MainActivity,"saved $result",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@MainActivity,"error not saved $result",Toast.LENGTH_SHORT).show()
            }
        }

    }


    companion object {
        private  const val Storage_Premission_Code =1
        private  const val Gallery =2

    }

}