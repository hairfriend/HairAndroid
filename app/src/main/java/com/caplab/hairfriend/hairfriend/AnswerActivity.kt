package com.caplab.hairfriend.hairfriend

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button

var infoOn :Boolean = false

class AnswerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        val infoBtn = findViewById<Button>(R.id.info_btn)
        supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout,FragmentSelect())
                .commit()

        infoBtn.setOnClickListener {
            if(infoOn){
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,FragmentInfo())
                        .commit()
                infoOn = !infoOn

            }
            else {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FragmentSelect())
                        .commit()
                infoOn = !infoOn
            }
        }


        Log.d("info",infoOn.toString())





    }


}
/*
private fun Button.setOnClickListener(): Boolean {
    if(infoOn){
        infoOn = false
        return infoOn
    }
    else{
        infoOn = true
        return infoOn
    }

}*/
