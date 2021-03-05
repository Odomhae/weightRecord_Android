package com.odom.weightrecord

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker

import com.odom.weightrecord.constants.WeightRecordConstants.CODE_GET_IMAGE_FILE
import com.odom.weightrecord.utils.WeightRecordUtil.displayImage
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import java.io.File
import java.util.*


class MainActivity : AppCompatActivity() {

    // 리스트뷰 아이템, 리스트뷰 어댑터
    val items = ArrayList<String>()
    val adapter by lazy {  ArrayAdapter(this, android.R.layout.select_dialog_item, items) }

    private var arrayThumbnails: ArrayList<Image> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 화면 관련 설정
        setLayoutActivity()
    }

    private fun setLayoutActivity() {

        listView_workout.adapter = adapter
        listView_workout.choiceMode = ListView.CHOICE_MODE_NONE
        
        listView_workout.setOnItemClickListener { parent, view, position, id ->

            showBox(items, position)
            setStringArrayPref("listData", items)
        }

        bt_add_image.setOnClickListener { addImg() }
        bt_input_workout.setOnClickListener { addList() }
//        bt_delete_image.setOnClickListener {
//            // 배열에서 삭제 .. 근데 굳이 배열일 필요가 있나? 하나만 선택하는데?
//            arrayThumbnails.clear()
//        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 이미지 선택
        if (resultCode == Activity.RESULT_OK) {
            val file = ImagePicker.getFile(data)!!

            val intent = Intent(this, ImageViewerActivity::class.java)
            intent.putExtra("fileName", file)
          //  ImageViewerActivity.start(this, file)
            Log.d("ttttt1",file.toString())
            startActivity(intent)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }

    }

    // 알림 박스에서 항목 수정 .. 필요한 기능인가?
    fun showBox(list :ArrayList<String>, position :Int) {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.input_box, null)
        val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

        val bt1 = mDialogView.findViewById(R.id.btdone) as Button
        val bt2 = mDialogView.findViewById(R.id.btdelete) as Button
        val inputEditText = mDialogView.findViewById(R.id.txtinput) as EditText
        inputEditText.setText(list[position])

        //show dialog
        val mAlertDialog = mBuilder.show()
        //login button click of custom layout
        bt1.setOnClickListener {
            list[position] = inputEditText.text.toString()
            setStringArrayPref("listData", list)
            adapter.notifyDataSetChanged()
            //dismiss dialog
            mAlertDialog.dismiss()
        }

        // 삭제
        bt2.setOnClickListener {

            //알림 & 화면 종료
            val builder = AlertDialog.Builder(this@MainActivity)

            builder.setTitle("삭제합니다")
                    .setPositiveButton("ok"
                    ) { _, _ ->
                        list.removeAt(position)
                        setStringArrayPref("listData", list)
                        adapter.notifyDataSetChanged()
                        mAlertDialog.dismiss()
                    }
                    .setNegativeButton("cancel"
                    ) { _, _ ->
                        mAlertDialog.dismiss()
                    }

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }


    private fun addImg() {

        ImagePicker.with(this)
            .crop()
            //.galleryOnly()
//                    .galleryMimeTypes(
//                            mimeTypes = arrayOf(
//                                    "image/png",
//                                    "image/jpg",
//                                    "image/jpeg"
//                            )
//                    )
            .maxResultSize(1080, 1920)
            .start()
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