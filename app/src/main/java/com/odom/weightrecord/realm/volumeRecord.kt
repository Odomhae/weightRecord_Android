package com.odom.weightrecord.realm

import com.google.gson.JsonArray
import com.odom.weightrecord.utils.ListViewItem
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.json.JSONArray


open class volumeRecord(

    @PrimaryKey
    var id: Long = 0L,

    // 날짜
    var date: Long = 0L,

    //운동 기록
    var workouts: ArrayList<ListViewItem> = ArrayList(),

    // 부위별 볼륨
    var vArm:Int = 0,
    var vBack:Int = 0,
    var vChest:Int = 0,
    var vShoulder:Int = 0,
    var vLeg :Int = 0,

    // 상하체 볼륨
    var vUpperBody :Int = 0,
    var vLowerBody :Int = 0,

    // 전체 볼륨
    var vTotal :Int= 0

) : RealmObject()