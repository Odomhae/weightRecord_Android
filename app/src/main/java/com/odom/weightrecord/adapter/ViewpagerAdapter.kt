package com.odom.weightrecord.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class FragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    var items = ArrayList<Fragment>()

    fun addItem(item: Fragment) {
        items.add(item)
    }

    override fun getItem(position: Int): Fragment {
        return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }
}