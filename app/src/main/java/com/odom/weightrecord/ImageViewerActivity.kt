package com.odom.weightrecord


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.odom.weightrecord.adapter.RecyclerviewAdapter
import com.odom.weightrecord.utils.ListViewItem
import com.odom.weightrecord.utils.WeightRecordUtil.setLocalImage
import kotlinx.android.synthetic.main.activity_image_viewer.*
import java.io.File
import java.io.Serializable


class ImageViewerActivity : AppCompatActivity() {

    // 운동 리스트뷰 데이터
    val workoutItems = ArrayList<ListViewItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image_viewer)

        setLayoutActivity()

        setRecyclerview()
    }

    private fun setLayoutActivity(){

        val file: Serializable? = intent.getSerializableExtra("fileName")

        Log.d("TAG1", file.toString())
        imgProfile.setLocalImage(file as File, imgProfile)

        bt_close.setOnClickListener { finish() }

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

    private fun setRecyclerview() {

        // 기록 리스트 가져오기
        val listPref = getStringArrayPref("listData")
        if(listPref.size > 0){
            for(value in listPref){
                workoutItems.add(value)
            }
        }

        recyclerView_img.adapter = RecyclerviewAdapter(listPref)
        recyclerView_img.layoutManager = LinearLayoutManager(this)

        recyclerView_img.setOnClickListener {
            Toast.makeText(applicationContext, "일단",Toast.LENGTH_SHORT).show()
            Log.d("TAG", "리사이클러뷰 클릭")
        }
    }

    // 저장된 배열 받아옴
    private fun getStringArrayPref(key: String): ArrayList<ListViewItem> {

        val prefs = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        val json = prefs.getString(key, null)
        val gson = Gson()

        val restoredData: ArrayList<ListViewItem> = gson.fromJson(json,
            object : TypeToken<ArrayList<ListViewItem?>>() {}.type
        )

        return restoredData
    }


}