package com.odom.weightrecord.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.odom.weightrecord.ImageViewerActivity
import com.odom.weightrecord.R


class MainFragment :Fragment() {

    private val arrFragments = arrayOfNulls<Fragment>(3)
    private var fragmentLetter: LetterFragment? = null
    private var fragmentImg: ImgFragment? = null
    private var fragmentList: ListFragment? = null

    private lateinit var vpContent : ViewPager

    var rlayout_tab_letter :  RelativeLayout ?= null
    var rlayout_tab_img :  RelativeLayout ?= null
    var rlayout_tab_list :  RelativeLayout ?= null
    var bt_add_image : Button?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val rootView: View = inflater.inflate(R.layout.fragment_main, container, false)

        rlayout_tab_letter = rootView.findViewById(R.id.rlayout_tab_letter) as RelativeLayout
        rlayout_tab_img = rootView.findViewById(R.id.rlayout_tab_img) as RelativeLayout
        rlayout_tab_list = rootView.findViewById(R.id.rlayout_tab_list) as RelativeLayout
        bt_add_image = rootView.findViewById(R.id.bt_add_image) as Button

        // 처음엔 letter fragment로 시작
        rlayout_tab_letter!!.isSelected = true

        fragmentLetter = LetterFragment.newInstance()
        fragmentImg = ImgFragment.newInstance()
        fragmentList = ListFragment.newInstance()

        // 각 뷰페이저
        arrFragments[0] = fragmentLetter
        arrFragments[1] = fragmentImg
        arrFragments[2] = fragmentList

        vpContent = rootView.findViewById(R.id.vp_content)
        vpContent.overScrollMode = View.OVER_SCROLL_NEVER
        vpContent.adapter = viewPagerAdapter(childFragmentManager)
        vpContent.offscreenPageLimit = 3
        vpContent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            //페이지 넘길
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) { setLayoutTab(position) }
        })

        rlayout_tab_letter!!.setOnClickListener { vpContent.currentItem = 0 }
        rlayout_tab_img!!.setOnClickListener { vpContent.currentItem = 1 }
        rlayout_tab_list!!.setOnClickListener { vpContent.currentItem = 2 }

        bt_add_image!!.setOnClickListener { addImg() }

        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 이미지 선택
        if (resultCode == Activity.RESULT_OK) {

            val file = ImagePicker.getFile(data)!!

            val intent = Intent(requireContext(), ImageViewerActivity::class.java)
            // 이미지 선택
            intent.putExtra("fileName", file)
            // 액티비티 실행
            startActivity(intent)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setLayoutTab(nPosition: Int) {

        // 눌린 곳만 체크
        rlayout_tab_letter!!.isSelected = (nPosition == 0)
        rlayout_tab_img!!.isSelected = (nPosition == 1)
        rlayout_tab_list!!.isSelected = (nPosition == 2)
    }

    private fun addImg() {

        val items = arrayOf<CharSequence>("앨범에서 가져오기", "사진찍기")

        val dialog: Dialog =
            AlertDialog.Builder(requireContext())
                .setItems(items) { _, position ->
                    selectImg(position)
                }.create()
        dialog.show()
    }

    private fun selectImg(position: Int) {

        if (position == 0) {
            ImagePicker.with(this).crop().galleryOnly().maxResultSize(1080, 1920).start()
        } else if (position == 1) {
            ImagePicker.with(this).crop().cameraOnly().start()
        }
    }

    internal inner class viewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment {
            return arrFragments[position]!!
        }

        override fun getCount(): Int {
            return arrFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return "Unknown"
        }
    }
}