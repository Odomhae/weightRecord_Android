package com.odom.weightrecord.realm

import com.odom.weightrecord.utils.ListViewItem
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlin.collections.ArrayList


open class volumeRecord(

    @PrimaryKey
    var id: Long = 0L,

    // 날짜
    var date: String?= null,

    //운동 기록
    //var workouts: ArrayList<ListViewItem> = ArrayList(),
    //var workouts :RealmList<routineList> = RealmList(),

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

) : RealmObject(){

    fun calUpper() :Int{
        vUpperBody += vArm
        vUpperBody += vBack
        vUpperBody += vChest
        vUpperBody += vShoulder

        return vUpperBody
    }

    fun calLower() :Int{
        vLowerBody = vLeg

        return vLowerBody
    }

    fun calTotal() :Int {
        vTotal = vLowerBody + vUpperBody

        return vTotal
    }
}