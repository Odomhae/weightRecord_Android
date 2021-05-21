package com.odom.weightrecord.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.odom.weightrecord.R
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment(){

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        val v = inflater.inflate(R.layout.fragment_calendar, container, false)
        val button :Button = v.findViewById(R.id.button)

        button.setOnClickListener {
            val days = calendarView.selectedDates
            val calendar = days[0]

            val day = calendar[Calendar.DAY_OF_MONTH]
            val month = calendar[Calendar.MONTH]
            val year = calendar[Calendar.YEAR]
            val week = SimpleDateFormat("EE").format(calendar.time)

            val day_full =
                    year.toString() + "년 " + (month + 1) + "월 " + day + "일 " + week + "요일"

            // 현지 시간 형식에 맞게
            // getBestDateTimePattern
            Toast.makeText(requireContext(), day_full, Toast.LENGTH_SHORT).show()
        }

        return v
    }
}