package com.odom.weightrecord

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.odom.weightrecord.adapter.ListviewAdapter
import com.odom.weightrecord.fragment.CalendarFragment
import com.odom.weightrecord.fragment.MainFragment
import com.odom.weightrecord.utils.ListViewItem
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    // 리스트뷰 아이템, 리스트뷰 어댑터
    //val items = ArrayList<ListViewItem>() // RealmList<routineList>()//
    //var listviewAdapter = ListviewAdapter()

   // var totalVolume = 0

    var vArm:Int = 0
    var vBack:Int = 0
    var vChest:Int = 0
    var vShoulder:Int = 0
    var vLeg :Int = 0

    var realm : Realm ?= null

    private var isFragmentB = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Realm.init(this)
        realm = Realm.getDefaultInstance()

        // 화면 관련 설정
       // setLayoutActivity()

        val fm: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fm.beginTransaction()
        fragmentTransaction.add(R.id.fLayout_fragment, MainFragment())
        fragmentTransaction.commit()

        val button1 = findViewById<View>(R.id.button1) as Button
        button1.setOnClickListener {
            switchFragment()
        }
    }

    fun switchFragment() {
        val fr: Fragment
        if (isFragmentB) {
            fr = CalendarFragment()
        } else {
            fr = MainFragment()
        }
        isFragmentB = if (isFragmentB) false else true

        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(R.id.fLayout_fragment, fr)
        fragmentTransaction.commit()
    }

//    private fun setLayoutActivity() {
//
//        listView_workout.adapter = listviewAdapter
//        listView_workout.choiceMode = ListView.CHOICE_MODE_NONE
//
//        listView_workout.setOnItemClickListener { _, _, position, _ ->
//            dialogUpdateDelete(items, position)
//            setStringArrayPref("listData", items)
//        }
//
//        tv_input_workout.setOnClickListener { addList() }
//        bt_add_image.setOnClickListener {
//
//            // 기록 db에 저장
//            // 코루틴으로 변환 요
//            val record = volumeRecord()
//            realm!!.beginTransaction()
//
//            // 오늘 날짜 -> 년 /월 /일 형식
//            val new_format = SimpleDateFormat("yyyy-MM-dd")
//            val date = new_format.format(Calendar.getInstance().time)
//            // 이걸 string으로 저장해도 되나
//            // 나중에 달력에 이거 기반으로 표시될텐데..
//            Log.d("==== 오늘 날짜 ", date.toString())
//            record.date = date
//
//            // 부위별 볼륨
//            record.vArm = vArm
//            record.vBack = vBack
//            record.vChest = vChest
//            record.vShoulder = vShoulder
//            record.vLeg = vLeg
//            record.vUpperBody = record.calUpper()
//            record.vLowerBody = record.calLower()
//            record.vTotal = record.calTotal()
//
//            // 운동 기록 realmlist로 해야하나 `
//            // TODO: 2021-04-20
//           // record.workouts = items
//
//            // id as Primary Key
//            record.id = System.currentTimeMillis()
//
//            realm!!.copyToRealm(record)
//            realm!!.commitTransaction()
//
//            // 사진추가
//            addImg()
//        }
//    }

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
        }

    }

    // 알림 박스에서 항목 수정 .. 필요한 기능인가?
//    private fun dialogUpdateDelete(list: ArrayList<ListViewItem>, position: Int) {
//
//        val mDialogView = LayoutInflater.from(this).inflate(R.layout.listview_update, null)
//        val mBuilder = AlertDialog.Builder(this)
//                .setView(mDialogView)
//
//        val bt1 = mDialogView.findViewById(R.id.bt_done) as Button
//        val bt2 = mDialogView.findViewById(R.id.bt_delete) as Button
//        val workoutPartText = mDialogView.findViewById(R.id.et_workoutPart_update) as EditText
//        val workoutNameText = mDialogView.findViewById(R.id.et_workoutName_update) as EditText
//        val weightText = mDialogView.findViewById(R.id.et_weight_update) as EditText
//        val repsText = mDialogView.findViewById(R.id.et_reps_update) as EditText
//
//        workoutPartText.setText(list[position].workoutPart)
//        workoutNameText.setText(list[position].workoutName)
//        weightText.setText(list[position].weight)
//        repsText.setText(list[position].reps)
//
//        val weight1 = list[position].weight!!.toInt() * list[position].reps!!.toInt()
//
//        val mAlertDialog = mBuilder.show()
//        // 수정
//        bt1.setOnClickListener {
//            list[position].workoutPart = workoutPartText.text.toString()
//            list[position].workoutName = workoutNameText.text.toString()
//            list[position].weight = weightText.text.toString()
//            list[position].reps = repsText.text.toString()
//
//            val weightUpdate = list[position].weight!!.toInt() * list[position].reps!!.toInt()
//            totalVolume += (-weight1 + weightUpdate)
//           // tv_weight_volume.text = "총 볼륨 : ${totalVolume}"
//
//            setStringArrayPref("listData", list)
//            listviewAdapter.updateReceiptsList(list)
//
//            mAlertDialog.dismiss()
//        }
//
//        // 삭제
//        bt2.setOnClickListener {
//
//            //알림 & 화면 종료
//            val builder = AlertDialog.Builder(this@MainActivity)
//
//            builder.setTitle("삭제합니다")
//                    .setPositiveButton(
//                            "ok"
//                    ) { _, _ ->
//                        val weight3 = list[position].weight!!.toInt()*list[position].reps!!.toInt()
//                        totalVolume -= weight3
//                 //       tv_weight_volume.text = "총 볼륨 : ${totalVolume}"
//
//                        list.removeAt(position)
//                        listviewAdapter.updateReceiptsList(list)
//
//                        mAlertDialog.dismiss()
//                    }
//                    .setNegativeButton(
//                            "cancel"
//                    ) { _, _ ->
//                        mAlertDialog.dismiss()
//                    }
//
//            val alertDialog = builder.create()
//            alertDialog.show()
//        }
//    }


//    private fun addImg() {
//
//        ImagePicker.with(this)
//            .crop()
//            .maxResultSize(1080, 1920)
//            .start()
//    }

    // 운동 추가
//    private fun addList(){
//
//        // 빈 입력 아니면 추가
//        if(et_workoutName.text.isEmpty()){
//            Toast.makeText(applicationContext, "종목값이 비었습니다", Toast.LENGTH_SHORT).show()
//        }
//        else{
//            val item1 = ListViewItem() // routineList()//
//            item1.workoutPart = sp_workoutPart.selectedItem.toString()
//            item1.workoutName = et_workoutName.text.toString()
//            item1.weight = et_weight.text.toString()
//            item1.reps = et_reps.text.toString()
//
//            items.add(item1)
//
//            // 배열로 저장
//            setStringArrayPref("listData", items)
//            et_workoutName.setText("")
//            et_weight.setText("")
//            et_reps.setText("")
//
//            // 운동 부위, 종목, 무게, 횟수 저장
//            listviewAdapter.addItem(
//                    item1.workoutPart.toString(), item1.workoutName.toString(),
//                    item1.weight.toString(), item1.reps.toString()
//            )
//            listviewAdapter.notifyDataSetChanged()
//
//            when(item1.workoutPart){
//                "등" -> {
//                    vBack += item1.weight!!.toInt() * item1.reps!!.toInt()
//                }
//                "가슴" -> {
//                    vChest += item1.weight!!.toInt() * item1.reps!!.toInt()
//                }
//                "팔" -> {
//                    vArm += item1.weight!!.toInt() * item1.reps!!.toInt()
//                }
//                "어깨" -> {
//                    vShoulder += item1.weight!!.toInt() * item1.reps!!.toInt()
//                }
//                "하체" -> {
//                    vLeg += item1.weight!!.toInt() * item1.reps!!.toInt()
//                }
//            }
//
//            totalVolume += item1.weight!!.toInt() * item1.reps!!.toInt()
//            tv_weight_volume.text = "총 볼륨 : ${totalVolume}"
//        }
//    }


    // JSON 배열로 저장
    fun setStringArrayPref(key: String, values: ArrayList<ListViewItem>) {

        val gson = Gson()
        val json = gson.toJson(values)
        val prefs = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putString(key, json)
        editor.apply()
    }

    // TODO: 2021-04-20  
//    val JobSaveRecord = CoroutineScope(Dispatchers.Main).launch {
//
//        CoroutineScope(Dispatchers.Default).async {
//
//        }.await()
//        
//    }

}