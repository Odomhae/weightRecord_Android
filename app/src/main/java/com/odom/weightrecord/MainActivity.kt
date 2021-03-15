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
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        bt_input_workout.setOnClickListener { addList() }
        bt_add_image.setOnClickListener { addImg() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 이미지 선택
        if (resultCode == Activity.RESULT_OK) {

            val file = ImagePicker.getFile(data)!!
            val listviewImage = getBitmapFromView(listView_workout)

            Log.d("TAG1",file.toString())
           //Log.d("TAG2",listviewImage.toString())

            val intent = Intent(this, ImageViewerActivity::class.java)
            // 이미지 선택
            intent.putExtra("fileName", file)
            // 리스트도 이미지로 변환해서
        //    intent.putExtra("listViewImg", listviewImage)
            // 액티비티 실행
            startActivity(intent)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }

    }

    // 리스트뷰 이미지로 변환.. 근데 이미지로 변환하면 크기 조절할때 글자가 깨질듯?
    private fun getBitmapFromView(view: View): Bitmap? {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable: Drawable = view.background

        bgDrawable.draw(canvas)

        view.draw(canvas)
        return returnedBitmap
    }

    // 알림 박스에서 항목 수정 .. 필요한 기능인가?
    private fun dialogUpdateDelete(list :ArrayList<ListViewItem>, position :Int) {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.listview_update, null)
        val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

        val bt1 = mDialogView.findViewById(R.id.bt_done) as Button
        val bt2 = mDialogView.findViewById(R.id.bt_delete) as Button
        val workoutNameText = mDialogView.findViewById(R.id.txt_workoutName) as EditText
        val weightText = mDialogView.findViewById(R.id.txt_weight) as EditText
        val repsText = mDialogView.findViewById(R.id.txt_reps) as EditText

        workoutNameText.setText(list[position].workoutName)
        weightText.setText(list[position].weight)
        repsText.setText(list[position].reps)

        val mAlertDialog = mBuilder.show()
        // 수정
        bt1.setOnClickListener {
            list[position].workoutName = workoutNameText.text.toString()
            list[position].weight = weightText.text.toString()
            list[position].reps = repsText.text.toString()

            Log.d("TAGGing", list[position].workoutName+ " "+ list[position].weight)

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
        if(txt_workoutName.text.isEmpty()){
            Toast.makeText(applicationContext, "종목값이 비었습니다", Toast.LENGTH_SHORT).show()
        }
        else{
            val item1 = ListViewItem()
            item1.workoutName = txt_workoutName.text.toString()
            item1.weight = txt_weight.text.toString()
            item1.reps = txt_reps.text.toString()

            items.add(item1)
            //for(i in 0 until items.size)
            // Log.d("TAG", items[i].workoutName.toString() + " "+ items[i].weight.toString())

            // 배열로 저장
            setStringArrayPref("listData", items)
            txt_workoutName.setText("")
            txt_weight.setText("")
            txt_reps.setText("")

            listviewAdapter.addItem(item1.workoutName.toString(), item1.weight.toString(), item1.reps.toString())
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