package com.odom.weightrecord.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.odom.weightrecord.R
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
            val clickedDayCalendar = eventDay.calendar
            Toast.makeText(requireContext(), clickedDayCalendar.toString(), Toast.LENGTH_SHORT).show()
        }

        button.setOnClickListener {

            if(calendarView.selectedDates.isEmpty()){
                Toast.makeText(requireContext(), "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()

            }else{

                val days = calendarView.selectedDates
                val calendar = days[0]

                val day = calendar[Calendar.DAY_OF_MONTH]
                val month = calendar[Calendar.MONTH]
                val year = calendar[Calendar.YEAR]

                val day_full =
                        year.toString() + "년 " + (month + 1) + "월 " + day + "일 "

                // 현지 시간 형식에 맞게
                // getBestDateTimePattern
                Toast.makeText(requireContext(), day_full, Toast.LENGTH_SHORT).show()

                // TODO: 2021-05-25
                // 뷰페이저 넘기기
                //val view2 = inflater.inflate(R.layout.activity_main, container, false)
                //val vp :ViewPager = view2.findViewById(R.id.viewPager)
                //vp.currentItem = 0
            }

        }

        return view
    }
}