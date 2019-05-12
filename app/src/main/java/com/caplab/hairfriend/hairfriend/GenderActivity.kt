package com.caplab.hairfriend.hairfriend

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_gender.*

class GenderActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gender)

        man_btn.setOnClickListener {
            val intent : Intent = Intent(this,selectActivity::class.java)
            val gender : String = "man"
            intent.putExtra("gender",gender)
            startActivity(intent)
        }
        woman_btn.setOnClickListener {
            val intent: Intent = Intent(this,selectActivity::class.java)
            val gender : String = "woman"
            intent.putExtra("gender",gender)
            startActivity(intent)
        }
    }

}