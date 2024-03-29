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
import com.google.gson.reflect.TypeToken
import com.odom.weightrecord.R
import com.odom.weightrecord.adapter.ListviewAdapter
import com.odom.weightrecord.utils.ListViewItem
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    // 리스트뷰 아이템, 리스트뷰 어댑터
    var items = ArrayList<ListViewItem>()
    var listviewAdapter = ListviewAdapter()

    var listView_workout : ListView?= null
    var tv_input_workout : TextView?= null
    var tv_clear_list : TextView?= null

    var itemsEmpty = ArrayList<ListViewItem>()

    companion object {
        fun newInstance() = ListFragment().apply {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val rootView: View = inflater.inflate(R.layout.fragment_list, container, false)

        listView_workout =  rootView.findViewById<View>(R.id.listView_workout) as ListView
        tv_input_workout =  rootView.findViewById<View>(R.id.tv_input_workout) as TextView
        tv_clear_list = rootView.findViewById(R.id.tv_clear_list) as TextView


        // 화면 관련 설정
        setLayoutFragment()

        return rootView
    }

    private fun setLayoutFragment() {

        // 리스트있으면 보여주기
        items = getStringArrayPref("listData")
        if(items.size > 0) {
           for(i in 0 until items.size){
               listviewAdapter.addItem(items[i].workoutName)
           }

            listviewAdapter.notifyDataSetChanged()
        }

        //리스트 초기화
        tv_clear_list!!.setOnClickListener {
            setStringArrayPref("listData", itemsEmpty)
            items.clear()
            listviewAdapter.updateReceiptsList(items)
            listviewAdapter.notifyDataSetChanged()
        }

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

        items.add(item1)

        // 배열로 저장
        setStringArrayPref("listData", items)
        et_workoutName.setText("")

        // 운동종목 저장
        listviewAdapter.addItem(item1.workoutName.toString())
        listviewAdapter.notifyDataSetChanged()

    }

    private fun dialogUpdateDelete(list: ArrayList<ListViewItem>, position: Int) {

        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.listview_update, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)

        val bt1 = mDialogView.findViewById(R.id.bt_done) as Button
        val bt2 = mDialogView.findViewById(R.id.bt_delete) as Button

        val workoutNameText = mDialogView.findViewById(R.id.et_workoutName_update) as EditText

        workoutNameText.setText(list[position].workoutName)

        val mAlertDialog = mBuilder.show()
        // 수정
        bt1.setOnClickListener {
            list[position].workoutName = workoutNameText.text.toString()

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


    // 저장된 배열 받아옴
    private fun getStringArrayPref(key: String): ArrayList<ListViewItem> {

        val prefs = requireActivity().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        val json = prefs.getString(key, null)
        val gson = Gson()

        val restoredData: ArrayList<ListViewItem> = gson.fromJson(json,
            object : TypeToken<ArrayList<ListViewItem?>>() {}.type
        )

        return restoredData
    }

}