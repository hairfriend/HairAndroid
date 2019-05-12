package com.caplab.hairfriend.hairfriend

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_loading.*
import android.graphics.drawable.AnimationDrawable
import android.content.Intent
import java.util.*


class LoadingActivity :AppCompatActivity() {
    lateinit var mTask:TimerTask
    lateinit var mTimer:Timer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)


//        var sex : String = intent.getStringExtra("sex")

        val frameAnimation = loading_iv.background as AnimationDrawable
        loading_iv.post(Runnable { frameAnimation.start() })

         mTask = object : TimerTask() {
            override fun run() {
                val intent = Intent(applicationContext, AnswerActivity::class.java)
                //intent.putExtra("sex",sex)
                startActivity(intent)
            }
        }

        mTimer = Timer()

        mTimer.schedule(mTask, 10000)




    }


}