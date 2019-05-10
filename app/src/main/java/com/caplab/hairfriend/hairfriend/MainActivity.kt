/*package com.caplab.hairfriend.hairfriend
import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.util.Log
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import android.os.Environment.getExternalStorageDirectory
import android.support.v4.content.FileProvider
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_selectca.*
import java.io.File
import java.nio.file.Files.createFile
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_ALBUM_PICKER = 0
    val URL:String = "http://117.16.231.66:7003"

    lateinit var mTask:TimerTask
    lateinit var mTimer:Timer
    lateinit var handler:Handler
    var TextCicle = false


    private val PERMISSION_REQUEST_CODE: Int = 101

    private var mCurrentPhotoPath: String? = null
    var mCurrentPhotoPathUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectca)

        gallery_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.type = "image/*"
            if(intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent,REQUEST_ALBUM_PICKER)
                //uploadImage()
            }

        }

        camera_btn.setOnClickListener {
            if (checkPersmission()) takePicture() else requestPermission()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    takePicture()

                } else {
                            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {

            }
        }
    }

    private fun takePicture() {

        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile()

        val uri: Uri = FileProvider.getUriForFile(
                this,
                "com.caplab.hairfriend.hairfriend.fileprovider",
                file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var selectSex : String = ""
            when ( requestCode){
                REQUEST_IMAGE_CAPTURE ->{
                    //To get the File for further usage
                    val auxFile = File(mCurrentPhotoPath)


                    //var bitmap: Bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
                    uploadImage(auxFile)
                    //imageView.setImageBitmap(bitmap)

                    val intent : Intent = Intent(this,LoadingActivity::class.java)
                    Log.d("select",selectSex.toString())

                    intent.putExtra("sex",selectSex)
                    startActivity(intent)
                }
                REQUEST_ALBUM_PICKER -> {
                    if (data != null) {
                        var albumFile : File? = null
                         albumFile = createFile()
                        var photoURI = data.getData()
                        var albumURI: Uri = Uri.fromFile(albumFile)
                        //val sendFile = File(albumURI)
                        uploadImage(File(albumURI.toString()))
                        val intent : Intent = Intent(this,LoadingActivity::class.java)
                        Log.d("select",selectSex.toString())

                        intent.putExtra("sex",selectSex)
                        startActivity(intent)
                    }
                    else {
                        finish()
                    }
                }

            }
        }
    }

    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, CAMERA), PERMISSION_REQUEST_CODE)
    }

    @Throws(IOException::class)
    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    fun getPath(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)

        cursor!!.moveToNext()

        val path = cursor.getString(cursor.getColumnIndex("_data"))

      //  c.close()
        return path
    }
    fun uploadImage(file:File) {

        val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        //var file :File = File(getPath(fileUri))

        // create RequestBody instance from file
        var requestFile: RequestBody
        requestFile = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                file
        );
        val retrofitInterface = retrofit.create(RetrofitInterface::class.java)

        //val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)

        val body = MultipartBody.Part.createFormData("avator", file.name, requestFile)
        val call = retrofitInterface.uploadImage(body)
        call.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                Toast.makeText(applicationContext, "저장완료", Toast.LENGTH_SHORT).show()
                val saveIntent = Intent()
                setResult(100, saveIntent)
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.w("error",t.toString())
            }
        })
    }


}
*/