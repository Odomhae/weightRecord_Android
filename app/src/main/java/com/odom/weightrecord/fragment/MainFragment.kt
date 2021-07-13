package com.odom.weightrecord.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.odom.weightrecord.ImageViewerActivity
import com.odom.weightrecord.R
import com.odom.weightrecord.adapter.ListviewAdapter
import com.odom.weightrecord.realm.volumeRecord
import com.odom.weightrecord.utils.ListViewItem
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainFragment :Fragment() {

    // 리스트뷰 아이템, 리스트뷰 어댑터
    val items = ArrayList<ListViewItem>() // RealmList<routineList>()//
    var listviewAdapter = ListviewAdapter()

    var totalVolume = 0

    var vArm:Int = 0
    var vBack:Int = 0
    var vChest:Int = 0
    var vShoulder:Int = 0
    var vLeg :Int = 0

    val realm = Realm.getDefaultInstance()

    var listView_workout : ListView ?= null
    var tv_input_workout : TextView ?= null
    var bt_add_image : Button ?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view: View = inflater.inflate(R.layout.fragment_main, container, false)

        listView_workout =  view.findViewById<View>(R.id.listView_workout) as ListView
        tv_input_workout =  view.findViewById<View>(R.id.tv_input_workout) as TextView
        bt_add_image = view.findViewById(R.id.bt_add_image) as Button


        // 화면 관련 설정
        setLayoutActivity()

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)

        // 이미지 선택
        if (resultCode == Activity.RESULT_OK) {

            val file = ImagePicker.getFile(data)!!

            val intent = Intent(requireContext(), ImageViewerActivity::class.java)
            // 이미지 선택
            intent.putExtra("fileName", file)
            // 액티비티 실행
            startActivity(intent)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setLayoutActivity() {

        listView_workout!!.choiceMode = ListView.CHOICE_MODE_NONE
        listView_workout!!.adapter = listviewAdapter

        listView_workout!!.setOnItemClickListener { _, _, position, _ ->
            dialogUpdateDelete(items, position)
            setStringArrayPref("listData", items)
        }

        tv_input_workout!!.setOnClickListener { addList() }
        bt_add_image!!.setOnClickListener {

            // 기록 db에 저장
            addRecord()

            // 사진추가
            addImg()
        }
    }

    // 코루틴으로 변환 요?
    private fun addRecord() {

        realm!!.beginTransaction()
        //val record = realm.createObject(volumeRecord::class.java)
        // id as Primary Key


        // 오늘 날짜 -> 년 /월 /일 형식
        val new_format = SimpleDateFormat("yyyy-MM-dd")
        val date = new_format.format(Calendar.getInstance().time)
        // 이걸 string으로 저장해도 되나
        // 나중에 달력에 이거 기반으로 표시될텐데..
        Log.d("==== 오늘 날짜 ", date.toString())
//        record.date = date
//
//        // 부위별 볼륨
//        record.vArm = vArm
//        record.vBack = vBack
//        record.vChest = vChest
//        record.vShoulder = vShoulder
//        record.vLeg = vLeg
//        record.vUpperBody = record.calUpper()
//        record.vLowerBody = record.calLower()
//        record.vTotal = record.calTotal()

        // 운동 기록 realmlist로 해야하나 `
        // TODO: 2021-04-20
        // record.workouts = items

        realm.commitTransaction()
    }


    private fun addImg() {

        val items = arrayOf<CharSequence>("앨범에서 가져오기", "사진찍기")

        val dialog: Dialog =
                AlertDialog.Builder(requireContext())
                        //.setTitle("title")
                        .setItems(items) { _, position ->

                            selectImg(position)
                        }.create()
        dialog.show()
    }

    private fun selectImg(position: Int) {

        if (position == 0) {
            ImagePicker.with(this).crop().galleryOnly().maxResultSize(1080, 1920).start()
        } else if (position == 1) {
            ImagePicker.with(this).crop().cameraOnly().start()
        }
    }

    private fun addList(){

        // 빈 입력 아니면 추가
        if(et_workoutName.text.isEmpty()){
            Toast.makeText(requireContext(), "종목값이 비었습니다", Toast.LENGTH_SHORT).show()
        }
        else{
            val item1 = ListViewItem()
            item1.workoutName = et_workoutName.text.toString()
            item1.weight = et_weight.text.toString()
            item1.reps = et_reps.text.toString()

            items.add(item1)

            // 배열로 저장
            setStringArrayPref("listData", items)
            et_workoutName.setText("")
            et_weight.setText("")
            et_reps.setText("")

            // 운동 부위, 종목, 무게, 횟수 저장
            listviewAdapter.addItem(
                    item1.workoutPart.toString(), item1.workoutName.toString(),
                    item1.weight.toString(), item1.reps.toString()
            )
            listviewAdapter.notifyDataSetChanged()

            when(item1.workoutPart){
                "등" -> {
                    vBack += item1.weight!!.toInt() * item1.reps!!.toInt()
                }
                "가슴" -> {
                    vChest += item1.weight!!.toInt() * item1.reps!!.toInt()
                }
                "팔" -> {
                    vArm += item1.weight!!.toInt() * item1.reps!!.toInt()
                }
                "어깨" -> {
                    vShoulder += item1.weight!!.toInt() * item1.reps!!.toInt()
                }
                "하체" -> {
                    vLeg += item1.weight!!.toInt() * item1.reps!!.toInt()
                }
            }

           // totalVolume += item1.weight!!.toInt() * item1.reps!!.toInt()
           // tv_weight_volume.text = "총 볼륨 : ${totalVolume}"
        }
    }

    private fun dialogUpdateDelete(list: ArrayList<ListViewItem>, position: Int) {

        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.listview_update, null)
        val mBuilder = AlertDialog.Builder(requireContext())
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

        val weight1 = list[position].weight!!.toInt() * list[position].reps!!.toInt()

        val mAlertDialog = mBuilder.show()
        // 수정
        bt1.setOnClickListener {
            list[position].workoutPart = workoutPartText.text.toString()
            list[position].workoutName = workoutNameText.text.toString()
            list[position].weight = weightText.text.toString()
            list[position].reps = repsText.text.toString()

            val weightUpdate = list[position].weight!!.toInt() * list[position].reps!!.toInt()
            totalVolume += (-weight1 + weightUpdate)
            // tv_weight_volume.text = "총 볼륨 : ${totalVolume}"

            setStringArrayPref("listData", list)
            listviewAdapter.updateReceiptsList(list)

            mAlertDialog.dismiss()
        }

        // 삭제
        bt2.setOnClickListener {

            //알림 & 화면 종료
            val builder = AlertDialog.Builder(requireContext())

            builder.setTitle("삭제합니다")
                    .setPositiveButton(
                            "ok"
                    ) { _, _ ->
                        val weight3 = list[position].weight!!.toInt()*list[position].reps!!.toInt()
                        totalVolume -= weight3
                        //       tv_weight_volume.text = "총 볼륨 : ${totalVolume}"

                        list.removeAt(position)
                        listviewAdapter.updateReceiptsList(list)

                        mAlertDialog.dismiss()
                    }
                    .setNegativeButton(
                            "cancel"
                    ) { _, _ ->
                        mAlertDialog.dismiss()
                    }

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    // JSON 배열로 저장
    fun setStringArrayPref(key: String, values: ArrayList<ListViewItem>) {

        val gson = Gson()
        val json = gson.toJson(values)
        val prefs = requireActivity().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putString(key, json)
        editor.apply()
    }


}