package com.odom.weightrecord.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.applandeo.materialcalendarview.EventDay
import com.odom.weightrecord.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class CalendarFragment : Fragment(){

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        val button :Button = view.findViewById(R.id.bt_add_record)
        val calendarView : com.applandeo.materialcalendarview.CalendarView = view.findViewById(R.id.calendarView)

        calendarView.setOnDayClickListener { eventDay ->

            // 클릭한 날
            val calendar = eventDay.calendar

            val day = calendar[Calendar.DAY_OF_MONTH]
            val month = calendar[Calendar.MONTH]
            val year = calendar[Calendar.YEAR]

            val day_full =
                    year.toString() + "년 " + (month + 1) + "월 " + day + "일 "

            // 현지 시간 형식에 맞게 // getBestDateTimePattern
            Toast.makeText(requireContext(), day_full, Toast.LENGTH_SHORT).show()
        }

        // 코루틴으로 할게 없나
        button.setOnClickListener {
            
        }

        return view
    }
}