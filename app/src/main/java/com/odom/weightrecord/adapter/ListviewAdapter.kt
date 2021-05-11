package com.odom.weightrecord.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.odom.weightrecord.R
import com.odom.weightrecord.utils.ListViewItem
import io.realm.RealmList

class ListviewAdapter : BaseAdapter() {

    private var listViewItemList = ArrayList<ListViewItem>() //RealmList<routineList>() //

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
        val context = parent!!.context

        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.listview_item, parent, false)
        }

        val item_workoutPart = view!!.findViewById(R.id.lv_item_workoutPart) as TextView
        val item_workoutName = view.findViewById(R.id.lv_item_workoutName) as TextView
        val item_weight = view.findViewById(R.id.lv_item_weight) as TextView
        val item_reps = view.findViewById(R.id.lv_item_reps) as TextView

        val listViewItem = listViewItemList[position]

        // 아이템 내 각 위젯에 데이터 반영
        item_workoutPart.text = listViewItem.workoutPart
        item_workoutName.text = listViewItem.workoutName

        // 부위구별 없을때 ex)한줄 메모
        if(listViewItem.workoutPart != "부위없음"){
            item_weight.text = listViewItem.weight + "kg"
            item_reps.text = listViewItem.reps + "번"
        }else{
            item_weight.text = listViewItem.weight
            item_reps.text = listViewItem.reps
        }



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
    fun addItem(workoutPart: String, workoutName: String, weight: String, reps: String) {
        val item = ListViewItem() //routineList()//

        item.workoutPart = workoutPart
        item.workoutName = workoutName
        item.weight = weight
        item.reps = reps

        listViewItemList.add(item)
    }

}