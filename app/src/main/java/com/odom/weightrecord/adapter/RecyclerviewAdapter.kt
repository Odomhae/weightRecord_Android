package com.odom.weightrecord.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.odom.weightrecord.R
import com.odom.weightrecord.utils.ListViewItem
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class RecyclerviewAdapter(private var data: ArrayList<ListViewItem>):RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.workoutName.text = data[position].workoutName

        if(data[position].workoutPart != "부위없음"){
            holder.weight.text = data[position].weight + "kg"
            holder.reps.text = data[position].reps + "번"
        }else{
            holder.weight.text = data[position].weight
            holder.reps.text = data[position].reps
        }


        // 폭 설정
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 80
        holder.itemView.requestLayout()
    }
}

class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
    var workoutName = view.rv_item_workoutName
    var weight = view.rv_item_weight
    var reps = view.rv_item_reps
}