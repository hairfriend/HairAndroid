package com.caplab.hairfriend.hairfriend

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_selectca.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.apache.commons.io.FileUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class selectActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE: Int = 101
    private val CAMERA_REQUEST_CODE : Int = 2
    private val GALLERY_REQUEST_CODE : Int = 1
    private val CODE_CROP_FROM_CAMERA : Int = 3
    private var imageview: ImageView? = null
    private var mCurrentPhotoPath: String? = null
    var mCurrentPhotoPathUri: Uri? = null
    var mPicImageURI : Uri? = null
    var ResultUri :Uri? = null
    var gender : String = ""
    lateinit var captureFile :File
    lateinit var thumbnail : Bitmap
    private val URL:String = "http://39.120.195.230:7003"

    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectca)
        gender = intent.getStringExtra("gender")


        gallery_btn.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
        }

        camera_btn.setOnClickListener{
            if(!checkPersmission()){
                requestPermission()
            }

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val file: File = createFile()

            mCurrentPhotoPathUri = FileProvider.getUriForFile(
                    this,
                    "com.caplab.hairfriend.hairfriend.fileprovider",
                    file
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPathUri)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GALLERY_REQUEST_CODE){
            if (data != null)
            {
                val contentURI = data.data
                try
                {
                    //val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    //val path = saveImage(bitmap)
                    captureFile = File(getPath(contentURI))
                    uploadImage(captureFile)
                    Toast.makeText(this@selectActivity, "처리시작!", Toast.LENGTH_SHORT).show()
                    /*val intent : Intent = Intent(this,LoadingActivity::class.java)
                    startActivity(intent)
                    *///imageview!!.setImageBitmap(bitmap)
                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@selectActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }
        }
        else if (requestCode == CAMERA_REQUEST_CODE)
        {
            mPicImageURI = if(mCurrentPhotoPathUri != null){
                mCurrentPhotoPathUri
            } else data!!.data

            var cropIntent: Intent = Intent("com.android.camera.action.CROP")

            if(mPicImageURI == null){
                try{
                    thumbnail = data!!.extras.get("data") as Bitmap
                    ResultUri = createCacheFile(this)
                    Toast.makeText(this@selectActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
                    cropIntent.setDataAndType(ResultUri,"image/*")
                    saveImage(thumbnail)
                    //imageview!!.setImageBitmap(thumbnail)

                }
                catch(e: IOException) {
                    return
                }
            }
            else{
                ResultUri = createCacheFile(this)
                cropIntent.setDataAndType(mPicImageURI,"image/*")
            }

            cropIntent.putExtra("outputX", 256)
            cropIntent.putExtra("outputY", 256)

            cropIntent.putExtra("aspectX",1)
            cropIntent.putExtra("aspectY",1)
            cropIntent.putExtra("scale",1)
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

            cropIntent.putExtra("output",ResultUri)
            startActivityForResult(cropIntent,CODE_CROP_FROM_CAMERA)
            startActivity(intent)
        }
        else if(requestCode == CODE_CROP_FROM_CAMERA){
            if(ResultUri != null){
                var options : BitmapFactory.Options = BitmapFactory.Options()
                var size : Int = 1

                while(true){
                    try{
                        options.inSampleSize = size
                        thumbnail = BitmapFactory.decodeFile(ResultUri?.path, options)
                        break
                    }
                    catch(e:OutOfMemoryError){
                        e.printStackTrace()
                        size++
                    }
                }
                captureFile = File(ResultUri!!.path)
                uploadImage(captureFile)
            }
        }
        else{
            finish()
        }
    }

    private fun createCacheFile(context: Context): Uri {
        var uri : Uri
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var url : String = "tmp_$timeStamp"
        uri = Uri.fromFile(File(context.externalCacheDir,url))
        return uri
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

    private fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                    .timeInMillis).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                    arrayOf(f.path),
                    arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)

            return f.absolutePath
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    private fun uploadImage(file:File) {

        val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        //var file :File = File(getPath(fileUri))
        // create RequestBody instance from file
        var requestFile: RequestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                file
        )
        val retrofitInterface = retrofit.create(RetrofitInterface::class.java)
        var Loadingintent : Intent = Intent(this,LoadingActivity::class.java)

        //val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)

        val body = MultipartBody.Part.createFormData("avator", file.name, requestFile)
        val genderRequest  = RequestBody.create(MediaType.parse("text/plain"), gender)
        val call = retrofitInterface.uploadImage(body,genderRequest)
        call.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                Toast.makeText(applicationContext, "저장완료", Toast.LENGTH_SHORT).show()
                val saveIntent = Intent()
                setResult(100, saveIntent)
                startActivity(Loadingintent)
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.w("error",t.toString())
            }
        })
    }

    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, CAMERA), PERMISSION_REQUEST_CODE)
    }
}
