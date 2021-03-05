package com.odom.weightrecord.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import java.io.File


object WeightRecordUtil {

    // 사진 등록
    fun displayImage(context: Context?, url: String?, imageView: ImageView) {
        try {
            if (url != null) {
                Glide.with(context!!)
                    .load(url)
                    .thumbnail(0.1f)
                    .skipMemoryCache(true)
                    .into(imageView)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 사진 등록
    fun ImageView.setLocalImage(file: File, imageView: ImageView) {
        Glide.with(this)
            .load(file)
            .into(imageView)

    }

}