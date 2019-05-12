package com.caplab.hairfriend.hairfriend;

import com.caplab.hairfriend.hairfriend.Response;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitInterface {
    @Multipart
    @POST("/imageUpload")
    Call<Response> uploadImage(@Part MultipartBody.Part avator, @Part("gender") RequestBody gender);
}