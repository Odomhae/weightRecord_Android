package com.odom.weightrecord

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    private val images = arrayListOf<Image>()

    // 리스트뷰 아이템, 리스트뷰 어댑터
    val items = ArrayList<String>()
    val adapter by lazy {  ArrayAdapter(this, android.R.layout.select_dialog_item, items) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list_workout.adapter = adapter

        button1.setOnClickListener {
            imagePicker.start()
        }

        bt_input_workout.setOnClickListener {
            addList()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            images.clear()
            images.addAll(ImagePicker.getImages(data))
            printImages(images)
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun printImages(images: List<Image>?) {
        if (images == null) return
        text1.text = images.joinToString("\n")
        text1.setOnClickListener {
            ImageViewerActivity.start(this@MainActivity, images)
        }
    }

    // 종목 추가버튼 함수
    private fun addList(){
        if(txt_exercise.text.isEmpty()){
            Toast.makeText(applicationContext, "종목값이 비었습니다", Toast.LENGTH_SHORT).show()
        }
        // 빈 입력 아니면 추가
        else{
            // 텍스트 추가
            items.add(txt_exercise.text.toString())

            // 배열로 저장
            setStringArrayPref("listData", items)
            txt_exercise.setText("")
            adapter.notifyDataSetChanged()
        }
    }



    private val imagePicker: ImagePicker
        get() {

            val imagePicker = ImagePicker.create(this)
                    .language("ko") // Set image picker language
                    .toolbarArrowColor(Color.RED) // set toolbar arrow up color
                    .toolbarFolderTitle("Folder") // folder selection title
                    .toolbarImageTitle("Tap to select") // image selection title
                    .toolbarDoneButtonText("Done") // done button text

               return imagePicker.imageDirectory("Camera") // captured image directory name ("Camera" folder by default)
                       .showCamera(true) // 카메라 이모티콘
                       .single() // 하나만 선택

        }


    // JSON 배열로 저장
    fun setStringArrayPref(key: String, values: ArrayList<String>) {

        val prefs = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()

        for (i in 0 until values.size) {
            a.put(values[i])
        }

        if (values.isNotEmpty()) {
            editor.putString(key, a.toString())

        } else {
            editor.putString(key, null)
        }

        editor.apply()
    }

}