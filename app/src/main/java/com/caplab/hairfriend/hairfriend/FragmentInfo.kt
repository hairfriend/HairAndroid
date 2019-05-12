package com.caplab.hairfriend.hairfriend

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.caplab.hairfriend.hairfriend.AnswerActivity.Companion.hairColor
import kotlinx.android.synthetic.main.activity_answer.*
import kotlinx.android.synthetic.main.fragment_color_info.*

class FragmentInfo: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_color_info,null)
        var infoiv = view.findViewById<ImageView>(R.id.info_iv)
        var infotvName = view.findViewById<TextView>(R.id.product_name_tv)
        when (hairColor) {
            "black" -> {
                Glide.with(this).load("http://39.120.195.230:7003/imageLoad/00001.jpg").into(activity?.mainImg)
                infoiv.setImageResource(R.drawable.blackcoloring)
                infotvName.text = "염색약 정보 : 미장센 헬로버블(블랙) 염색약"
            }
            "brown" -> {
                Glide.with(this).load("http://39.120.195.230:7003/imageLoad/00003.jpg").into(activity?.mainImg)
                infoiv.setImageResource(R.drawable.browncoloring)
                infotvName.text = "염색약 정보 : 미장센 헬로버블(브라운) 염색약"
            }
            "blond" -> {
                Glide.with(this).load("http://39.120.195.230:7003/imageLoad/00002.jpg").into(activity?.mainImg)
                infoiv.setImageResource(R.drawable.blondcoloring)
                infotvName.text = "염색약 정보 : 미장센 헬로버블(금발) 염색약"
            }
        }
        return view
    }
}