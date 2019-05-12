package com.caplab.hairfriend.hairfriend


import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.caplab.hairfriend.hairfriend.AnswerActivity.Companion.hairColor
import kotlinx.android.synthetic.main.activity_answer.*
import kotlinx.android.synthetic.main.activity_answer.view.*
import kotlinx.android.synthetic.main.fragment_color_select.*


class FragmentSelect: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_color_select,null)
        var blackBtn = view.findViewById(R.id.constraintLayout2) as ConstraintLayout
        var brownBtn = view.findViewById(R.id.constraintLayout4) as ConstraintLayout
        var blondBtn = view.findViewById(R.id.constraintLayout3) as ConstraintLayout

        blackBtn.setOnClickListener{
            Glide.with(this).load("http://39.120.195.230:7003/imageLoad/00001.jpg").into(activity?.mainImg)
            hairColor = "black"
            //activity?.mainImg?.setImageResource(R.drawable.womanblack)
        }
        brownBtn.setOnClickListener{
            Glide.with(this).load("http://39.120.195.230:7003/imageLoad/00003.jpg").into(activity?.mainImg)
            hairColor = "brown"
            //activity?.mainImg?.setImageResource(R.drawable.womanbrown)
        }

        blondBtn.setOnClickListener {
            Glide.with(this).load("http://39.120.195.230:7003/imageLoad/00002.jpg").into(activity?.mainImg)
            hairColor = "blond"
            //activity?.mainImg?.setImageResource(R.drawable.womanblond)
        }

        return view
    }
}

private fun AppCompatImageView.setOnClickListener() {

}
