package com.odom.weightrecord


import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.odom.weightrecord.adapter.FragmentAdapter
import com.odom.weightrecord.fragment.*
import io.realm.Realm


class MainActivity : FragmentActivity() {

    private val arrFragments = arrayOfNulls<Fragment>(3)
    private var fragmentLetter: LetterFragment? = null
    private var fragmentImg: ImgFragment? = null
    private var fragmentList: ListFragment? = null

    private lateinit var vpContent : ViewPager

    var rlayout_tab_letter :  RelativeLayout?= null
    var rlayout_tab_img :  RelativeLayout?= null
    var rlayout_tab_list :  RelativeLayout?= null
    var bt_add_image : Button?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vPager : ViewPager = findViewById(R.id.viewPager)
        vPager.offscreenPageLimit = 2

        val adapter = FragmentAdapter(supportFragmentManager)

        adapter.addItem(MainFragment())

        vPager.adapter = adapter
    }

}