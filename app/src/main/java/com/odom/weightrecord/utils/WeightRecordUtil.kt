package com.odom.weightrecord.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import java.io.ByteArrayOutputStream
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

    // 뷰를 비트맵으로 변환
    fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        view.draw(canvas)
        return bitmap
    }

    // 비트맵 이미지 Uri 가져오기
    fun getImageUri(context: Context, image: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val path: String = MediaStore.Images.Media.insertImage(
                context.contentResolver, image, "Title", null )

        return Uri.parse(path)
    }

}