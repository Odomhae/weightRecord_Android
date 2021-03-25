package com.odom.weightrecord.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.odom.weightrecord.R
import com.odom.weightrecord.utils.ListViewItem

class ListviewAdapter : BaseAdapter() {

    private var listViewItemList = ArrayList<ListViewItem>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var view = convertView
        val context = parent!!.context

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.listview_item, parent, false)
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        val item_workoutName = view!!.findViewById(R.id.item_workoutName) as TextView
        val item_weight = view.findViewById(R.id.item_weight) as TextView
        val item_reps = view.findViewById(R.id.item_reps) as TextView

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        val listViewItem = listViewItemList[position]

        // 아이템 내 각 위젯에 데이터 반영
        item_workoutName.text = listViewItem.workoutName
        item_weight.text = listViewItem.weight+ "kg"
        item_reps.text = listViewItem.reps+ "번"

        return view
    }

    override fun getItem(position: Int): Any {
        return listViewItemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listViewItemList.size
    }

    fun updateReceiptsList(newlist: ArrayList<ListViewItem>) {
        listViewItemList.clear()
        listViewItemList.addAll(newlist)
        this.notifyDataSetChanged()

    }

    // 아이템 데이터 추가를 위한 함수
    fun addItem(workoutName: String, weight: String, reps: String) {
        val item = ListViewItem()

        item.workoutName = workoutName
        item.weight = weight
        item.reps = reps

        listViewItemList.add(item)
    }

}