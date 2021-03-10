package com.odom.weightrecord


import android.R.attr.path
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.odom.weightrecord.utils.WeightRecordUtil.setLocalImage
import kotlinx.android.synthetic.main.activity_image_viewer.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.Serializable


class ImageViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image_viewer)

        val file: Serializable? = intent.getSerializableExtra("fileName")
//        val listViewImag = intent.extras!!.get("listViewImg") as Bitmap

        Log.d("TAG1", file.toString())
  //      Log.d("TAG2", listViewImag.toString())
        imgProfile.setLocalImage(file as File, imgProfile)
        //displayImage(this, (file as File).path, imgProfile)

        button2.setOnClickListener { finish() }

        bt_save.setOnClickListener {  }

        bt_share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val screenshotUri: Uri = Uri.parse(file.toString())

            intent.type=("image/png")
            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
            startActivity(Intent.createChooser(intent, "Share image using")); // 변경가능

           // if(getIntent().resolveActivity(packageManager) != null) {
            //    startActivity(intent)
            //}
        }
    }

}