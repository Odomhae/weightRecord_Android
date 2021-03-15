package com.odom.weightrecord


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.odom.weightrecord.utils.ListViewItem
import com.odom.weightrecord.utils.WeightRecordUtil.setLocalImage
import kotlinx.android.synthetic.main.activity_image_viewer.*
import java.io.File
import java.io.Serializable


class ImageViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image_viewer)

        setLayoutActivity()

        // 기록 리스트 가져오기
        val workoutData = getStringArrayPref("listData")
        if(workoutData.size > 0){
            for(i in 0 until workoutData.size)
                Log.d("TAG", workoutData[i].workoutName.toString())

        }

    }

    fun setLayoutActivity(){

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

    // 저장된 배열 받아옴
    fun getStringArrayPref(key: String): ArrayList<ListViewItem> {

        val prefs = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        val json = prefs.getString(key, null)
        val gson = Gson()

        val restoredData: ArrayList<ListViewItem> = gson.fromJson(json,
            object : TypeToken<ArrayList<ListViewItem?>>() {}.type
        )

        return restoredData
    }


}