package com.odom.weightrecord

import android.content.Context
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.odom.weightrecord.utils.WeightRecordUtil.displayImage
import kotlinx.android.synthetic.main.activity_image_viewer.*
import java.io.File
import java.io.Serializable
import java.util.ArrayList

class ImageViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image_viewer)

        val file: Serializable? = intent.getSerializableExtra("fileName")

        Log.d("TTTTt123", file.toString())
        imgProfile.setLocalImage(file as File, imgProfile)
        displayImage(this, file.path, imgProfile)

        button2.setOnClickListener { finish() }
    }


    fun ImageView.setLocalImage(file: File, imageView: ImageView) {
        Glide.with(this)
            .load(file)
            .into(imageView)

    }
}