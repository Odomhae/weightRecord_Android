package com.odom.weightrecord.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.odom.weightrecord.R
import com.odom.weightrecord.utils.ListViewItem
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class RecyclerviewAdapter_black(private var data: ArrayList<ListViewItem>):RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_black, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.workoutName.text = data[position].workoutName

        // 폭 설정
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 80
        holder.itemView.requestLayout()
    }
}