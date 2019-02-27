package com.caplab.hairfriend.hairfriend

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_btn_album.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            if(intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent,1)
            }
        }

        main_btn_selfie.setOnClickListener {
            val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var permissionCamera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
            if(permissionCamera == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.CAMERA),
                        1);

            }
            else {
                if(intent1.resolveActivity(packageManager) != null){
                    startActivityForResult(intent1,0)
                }
            }
        }
    }


}
