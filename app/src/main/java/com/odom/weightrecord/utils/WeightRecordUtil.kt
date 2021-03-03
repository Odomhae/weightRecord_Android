package com.odom.weightrecord.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide


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

}