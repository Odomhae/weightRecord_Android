package com.odom.weightrecord


import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.odom.weightrecord.adapter.FragmentAdapter
import com.odom.weightrecord.fragment.CalendarFragment
import com.odom.weightrecord.fragment.MainFragment
import io.realm.Realm


class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Realm.init(this)

        val vPager : ViewPager = findViewById(R.id.viewPager)
        vPager.offscreenPageLimit = 2

        val adapter = FragmentAdapter(supportFragmentManager)

        adapter.addItem(MainFragment())
        adapter.addItem(CalendarFragment())

        vPager.adapter = adapter
    }

}