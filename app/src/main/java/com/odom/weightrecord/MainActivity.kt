package com.odom.weightrecord

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.odom.weightrecord.adapter.ListviewAdapter
import com.odom.weightrecord.fragment.CalendarFragment
import com.odom.weightrecord.fragment.MainFragment
import com.odom.weightrecord.utils.ListViewItem
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    // 리스트뷰 아이템, 리스트뷰 어댑터
    //val items = ArrayList<ListViewItem>() // RealmList<routineList>()//
    //var listviewAdapter = ListviewAdapter()

   // var totalVolume = 0

    var vArm:Int = 0
    var vBack:Int = 0
    var vChest:Int = 0
    var vShoulder:Int = 0
    var vLeg :Int = 0

    var realm : Realm ?= null

    private var isFragmentB = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Realm.init(this)
        realm = Realm.getDefaultInstance()


        val fm: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fm.beginTransaction()
        fragmentTransaction.add(R.id.fLayout_fragment, MainFragment())
        fragmentTransaction.commit()

        val button1 = findViewById<View>(R.id.button1) as Button
        button1.setOnClickListener {
            switchFragment()
        }
    }

    fun switchFragment() {
        val fr: Fragment = if (isFragmentB) {
            CalendarFragment()
        } else {
            MainFragment()
        }
        isFragmentB = !isFragmentB

        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(R.id.fLayout_fragment, fr)
        fragmentTransaction.commit()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // 이미지 선택
//        if (resultCode == Activity.RESULT_OK) {
//
//            val file = ImagePicker.getFile(data)!!
//
//            val intent = Intent(this, ImageViewerActivity::class.java)
//            // 이미지 선택
//            intent.putExtra("fileName", file)
//            // 액티비티 실행
//            startActivity(intent)
//
//        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//        }
//
//    }



}