package com.odom.weightrecord.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.odom.weightrecord.R
import com.odom.weightrecord.utils.ListViewItem

class LetterFragment : Fragment(){

    var inputText : TextView?= null
    var saveButton : Button? = null

    companion object {
        fun newInstance() = LetterFragment().apply {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView: View = inflater.inflate(R.layout.fragment_letter, container, false)

        inputText = rootView.findViewById(R.id.tv_letter)
        saveButton = rootView.findViewById(R.id.bt_letter_save)

        val a = inputText!!.text.toString()

        Log.d("====a" , a.toString())

        // 화면 관련 설정
        setLayoutFragment()

        return rootView
    }

    private fun setLayoutFragment() {

        // 글 임시저장
        saveButton!!.setOnClickListener {

            setStringPref("letter" , inputText!!.text.toString())

        }


    }

    // 글 저장
    private fun setStringPref(key: String, values: String) {

        val prefs = requireContext().getSharedPreferences("LETTER", Context.MODE_PRIVATE)
        with(prefs.edit()){
            putString(key, values)
            commit()
        }

    }
}