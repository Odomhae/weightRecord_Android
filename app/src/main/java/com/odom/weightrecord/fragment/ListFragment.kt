package com.odom.weightrecord.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.odom.weightrecord.R
import com.odom.weightrecord.adapter.ListviewAdapter
import com.odom.weightrecord.utils.ListViewItem
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    // 리스트뷰 아이템, 리스트뷰 어댑터
    val items = ArrayList<ListViewItem>()
    var listviewAdapter = ListviewAdapter()

    var listView_workout : ListView?= null
    var tv_input_workout : TextView?= null

    companion object {
        fun newInstance() = ListFragment().apply {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val rootView: View = inflater.inflate(R.layout.fragment_list, container, false)

        listView_workout =  rootView.findViewById<View>(R.id.listView_workout) as ListView
        tv_input_workout =  rootView.findViewById<View>(R.id.tv_input_workout) as TextView

        // 화면 관련 설정
        setLayoutActivity()

        return rootView
    }

    private fun setLayoutActivity() {

        listView_workout!!.choiceMode = ListView.CHOICE_MODE_NONE
        listView_workout!!.adapter = listviewAdapter

        listView_workout!!.setOnItemClickListener { _, _, position, _ ->
            dialogUpdateDelete(items, position)
            setStringArrayPref("listData", items)
        }

        tv_input_workout!!.setOnClickListener { addList() }
    }

    private fun addList(){

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
            val builder = AlertDialog.Builder(requireContext())

            builder.setTitle("삭제합니다")
                .setPositiveButton(
                    "확인") { _, _ ->
                    list.removeAt(position)
                    listviewAdapter.updateReceiptsList(list)
                    mAlertDialog.dismiss()
                }
                .setNegativeButton(
                    "취소") { _, _ ->
                    mAlertDialog.dismiss()
                }

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    // JSON 배열로 저장
    private fun setStringArrayPref(key: String, values: ArrayList<ListViewItem>) {

        val gson = Gson()
        val json = gson.toJson(values)
        val prefs = requireActivity().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putString(key, json)
        editor.apply()
    }
}