package com.odom.weightrecord


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.odom.weightrecord.adapter.RecyclerviewAdapter
import com.odom.weightrecord.adapter.RecyclerviewAdapter_black
import com.odom.weightrecord.utils.ListViewItem
import com.odom.weightrecord.utils.WeightRecordUtil.getImageUri
import com.odom.weightrecord.utils.WeightRecordUtil.setLocalImage
import com.odom.weightrecord.utils.WeightRecordUtil.viewToBitmap
import kotlinx.android.synthetic.main.activity_image_viewer.*
import java.io.*
import kotlin.math.max
import kotlin.math.min


class ImageViewerActivity : AppCompatActivity() {

    // 운동 리스트뷰 데이터
    val workoutItems = ArrayList<ListViewItem>()

    var listPref = ArrayList<ListViewItem>()

    var startX = 0f
    var startY = 0f
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image_viewer)
        getData()
        setLayoutActivity()
        setRecyclerview()
    }

    private fun getData(){

        // 기록 리스트 가져오기
        listPref = getStringArrayPref("listData")
        if(listPref.size > 0){
            for(value in listPref){
                workoutItems.add(value)
            }
        }

    }

    private fun setLayoutActivity(){

        val file: Serializable? = intent.getSerializableExtra("fileName")

        Log.d("TAG", file.toString())
        imgProfile.setLocalImage(file as File, imgProfile)

        // 뒤로가기
        bt_close.setOnClickListener { finish() }

        // 공유하기
        // 이미지 + 리사이클러뷰있는 프레임레이아웃을 공유
        bt_share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val screenshotUri: Uri = Uri.parse(getImageUri(this, viewToBitmap(frameLayout)).toString())

            intent.type = ("image/*")
            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
            startActivity(Intent.createChooser(intent, "Share image"))
        }

        var cntClicked = 0
        // 글자색 변경
        bt_txtColor.setOnClickListener {
            cntClicked++
            if(cntClicked %2 == 1){
                recyclerView_img.adapter = RecyclerviewAdapter_black(listPref)
                recyclerView_img.layoutManager = LinearLayoutManager(this)

            }else{
                recyclerView_img.adapter = RecyclerviewAdapter(listPref)
                recyclerView_img.layoutManager = LinearLayoutManager(this)

            }

        }

        val prefs = getSharedPreferences("LETTER", Context.MODE_PRIVATE)
        val a = prefs.getString("letter" , "aaaz")
        tv_img.text = a

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setRecyclerview() {

        recyclerView_img.adapter = RecyclerviewAdapter(listPref)
        recyclerView_img.layoutManager = LinearLayoutManager(this)

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        // 리스트 눌러서 이동
        recyclerView_img.setOnTouchListener { v, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    startY = event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val movedX: Float = event.x - startX
                    val movedY: Float = event.y - startY

                    v.x = v.x + movedX
                    v.y = v.y + movedY
                }
            }
            true
        }

        // 텍스튜뷰 눌러서 이동
        tv_img.setOnTouchListener { v, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    startY = event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val movedX: Float = event.x - startX
                    val movedY: Float = event.y - startY

                    v.x = v.x + movedX
                    v.y = v.y + movedY
                }
            }

            true
        }
    }



    override fun onTouchEvent(event: MotionEvent?): Boolean {

        scaleGestureDetector?.onTouchEvent(event)
        return true
    }

    inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {

            scaleFactor *= scaleGestureDetector.scaleFactor

            // 최소 0.5, 최대 4배
            scaleFactor = max(0.5f, min(scaleFactor, 4.0f))

            // 리사이클러뷰에 적용
            recyclerView_img.scaleX = scaleFactor
            recyclerView_img.scaleY = scaleFactor

            // 텍스트뷰에 적용
            tv_img.scaleX = scaleFactor
            tv_img.scaleY = scaleFactor

            return true
        }
    }

    // 저장된 배열 받아옴
    private fun getStringArrayPref(key: String): ArrayList<ListViewItem> {

        val prefs = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        val json = prefs.getString(key, null)
        val gson = Gson()

        val restoredData: ArrayList<ListViewItem> = gson.fromJson(json,
            object : TypeToken<ArrayList<ListViewItem?>>() {}.type
        )

        return restoredData
    }

    // 저장된 글 가져옴
//    private fun getStringPref(key: String) {
//
//        val prefs = getSharedPreferences("LETTER", Context.MODE_PRIVATE)
//        val a = prefs.getString("letter" , "aaaz")
//        with(prefs.edit()){
//            getStringPref(key)
//            commit()
//        }
//
//    }

}