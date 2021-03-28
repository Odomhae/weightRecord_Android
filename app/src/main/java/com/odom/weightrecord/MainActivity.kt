package com.odom.weightrecord

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.odom.weightrecord.adapter.ListviewAdapter
import com.odom.weightrecord.utils.ListViewItem
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    // 리스트뷰 아이템, 리스트뷰 어댑터
    val items = ArrayList<ListViewItem>()
    var listviewAdapter = ListviewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 화면 관련 설정
        setLayoutActivity()
    }

    private fun setLayoutActivity() {

        listView_workout.adapter = listviewAdapter
        listView_workout.choiceMode = ListView.CHOICE_MODE_NONE
        
        listView_workout.setOnItemClickListener { _, _, position, _ ->
            dialogUpdateDelete(items, position)
            setStringArrayPref("listData", items)
        }

        tv_input_workout.setOnClickListener { addList() }
        bt_add_image.setOnClickListener { addImg() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 이미지 선택
        if (resultCode == Activity.RESULT_OK) {

            val file = ImagePicker.getFile(data)!!

            val intent = Intent(this, ImageViewerActivity::class.java)
            // 이미지 선택
            intent.putExtra("fileName", file)
            // 액티비티 실행
            startActivity(intent)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }

    }

    // 알림 박스에서 항목 수정 .. 필요한 기능인가?
    private fun dialogUpdateDelete(list :ArrayList<ListViewItem>, position :Int) {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.listview_update, null)
        val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

        val bt1 = mDialogView.findViewById(R.id.bt_done) as Button
        val bt2 = mDialogView.findViewById(R.id.bt_delete) as Button
        val workoutPartText = mDialogView.findViewById(R.id.et_workoutPart_update) as EditText
        val workoutNameText = mDialogView.findViewById(R.id.et_workoutName_update) as EditText
        val weightText = mDialogView.findViewById(R.id.et_weight_update) as EditText
        val repsText = mDialogView.findViewById(R.id.et_reps_update) as EditText

        workoutPartText.setText(list[position].workoutPart)
        workoutNameText.setText(list[position].workoutName)
        weightText.setText(list[position].weight)
        repsText.setText(list[position].reps)

        val mAlertDialog = mBuilder.show()
        // 수정
        bt1.setOnClickListener {
            list[position].workoutPart = workoutPartText.text.toString()
            list[position].workoutName = workoutNameText.text.toString()
            list[position].weight = weightText.text.toString()
            list[position].reps = repsText.text.toString()

            setStringArrayPref("listData", list)
            listviewAdapter.updateReceiptsList(list)

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
                        listviewAdapter.updateReceiptsList(list)

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

    // 운동 추가
    private fun addList(){

        // 빈 입력 아니면 추가
        if(et_workoutName.text.isEmpty()){
            Toast.makeText(applicationContext, "종목값이 비었습니다", Toast.LENGTH_SHORT).show()
        }
        else{
            val item1 = ListViewItem()
            item1.workoutPart = sp_workoutPart.selectedItem.toString()
            item1.workoutName = et_workoutName.text.toString()
            item1.weight = et_weight.text.toString()
            item1.reps = et_reps.text.toString()

            items.add(item1)

            // 배열로 저장
            setStringArrayPref("listData", items)
            et_workoutName.setText("")
            et_weight.setText("")
            et_reps.setText("")

            // 운동 부위, 종목, 무게, 횟수
            listviewAdapter.addItem(item1.workoutPart.toString(),  item1.workoutName.toString(),
                    item1.weight.toString(), item1.reps.toString())
            listviewAdapter.notifyDataSetChanged()
        }
    }


    // JSON 배열로 저장
    fun setStringArrayPref(key: String, values: ArrayList<ListViewItem>) {

        val gson = Gson()
        val json = gson.toJson(values)
        val prefs = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putString(key, json)
        editor.apply()
    }

}