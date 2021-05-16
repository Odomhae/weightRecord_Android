package com.odom.weightrecord.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RadioGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.applikeysolutions.cosmocalendar.utils.SelectionType
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
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
//            R.id.clear_selections -> {
//                clearSelectionsMenuClick()
//                true
//            }
            1 -> {
                val days = calendarView.selectedDates
                Log.d("====days", days.toString())
                var result = ""
                var i = 0
                while (i < days.size) {
                    val calendar = days[i]
                    val day = calendar[Calendar.DAY_OF_MONTH]
                    val month = calendar[Calendar.MONTH]
                    val year = calendar[Calendar.YEAR]
                    val week = SimpleDateFormat("EE").format(calendar.time)
                    val day_full =
                        year.toString() + "년 " + (month + 1) + "월 " + day + "일 " + week + "요일"
                    result += """
                         $day_full
                         
                         """.trimIndent()
                    i++
                }
                Log.d("======result", result.toString())
                Toast.makeText(requireContext(), result, Toast.LENGTH_LONG).show()
                true
            }
            else -> {

                Log.d("====days", calendarView.selectedDates.toString())
                true
            }
        }
    }
}