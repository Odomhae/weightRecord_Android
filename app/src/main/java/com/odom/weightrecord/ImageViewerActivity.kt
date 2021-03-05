package com.odom.weightrecord


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.odom.weightrecord.utils.WeightRecordUtil.displayImage
import com.odom.weightrecord.utils.WeightRecordUtil.setLocalImage
import kotlinx.android.synthetic.main.activity_image_viewer.*
import java.io.File
import java.io.Serializable

class ImageViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image_viewer)

        val file: Serializable? = intent.getSerializableExtra("fileName")

        Log.d("TAG", file.toString())
        imgProfile.setLocalImage(file as File, imgProfile)
        //displayImage(this, (file as File).path, imgProfile)

        button2.setOnClickListener { finish() }
    }



}