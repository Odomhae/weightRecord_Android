package com.odom.weightrecord

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.odom.weightrecord.constants.WeightRecordConstants.CODE_GALLERY
import com.odom.weightrecord.constants.WeightRecordConstants.CODE_TAKE_PICTURE
import com.odom.weightrecord.utils.WeightRecordUtil.displayImage
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
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

        list_workout.adapter = adapter

        bt_add_image.setOnClickListener { addImg() }
        bt_input_workout.setOnClickListener { addList() }
        bt_delete_image.setOnClickListener {

            // 기존 이미지 삭제하기
            report_write_iv_image.setImageResource(0)
            // 배열에서 삭제 .. 근데 굳이 배열일 필요가 있나? 하나만 선택하는데?
            arrayThumbnails.clear()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        //앨범 이미지 선택
        if (requestCode == CODE_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                val images: List<Image> = ImagePicker.getImages(data)

                if (images.isNotEmpty()) {
                    arrayThumbnails.addAll(images)
                    setThumbnails(arrayThumbnails)
                }
            }

            //카메라 촬영
        } else if (requestCode == CODE_TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                val images: List<Image> = ImagePicker.getImages(data)

                if (images.isNotEmpty()) {
                    arrayThumbnails.addAll(images)
                    setThumbnails(arrayThumbnails)
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun addImg() {

        val items = arrayOf<CharSequence>("앨범", "사진 촬영")

        val dialog: Dialog =
                AlertDialog.Builder(this)
                        .setTitle("사진 선택하기")
                        .setItems(items) { dialog, which ->
                            changeImg(which)
                        }
                        .setOnKeyListener { dialog, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        //nothing
                    }
                    false
                }.create()

        dialog.show()
    }

    private fun changeImg(selectDialogPostion: Int) {

        // 앨범에서 가져오기
        if (selectDialogPostion == 0) {
            ImagePicker.create(this)
                    .single() // 하나만 선택
                    .showCamera(false) // multi mode (default mode)
                    .start(CODE_GALLERY) // start image picker activity with request code

        //  사진 촬영
        } else if (selectDialogPostion == 1) {
            ImagePicker
                    .cameraOnly()
                    .start(this, CODE_TAKE_PICTURE)
        }
    }

    // 이미지 추가
    private fun setThumbnails(arrayThumbnails: ArrayList<Image>) {
        report_write_iv_image!!.visibility = View.INVISIBLE

        displayImage(this, arrayThumbnails[0].path, report_write_iv_image)
        report_write_iv_image!!.visibility = View.VISIBLE
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