package com.odom.weightrecord.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.odom.weightrecord.R

class LetterFragment : Fragment(){

    companion object {
        fun newInstance() = LetterFragment().apply {  }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView: View = inflater.inflate(R.layout.fragment_letter, container, false)


        return rootView
    }
}