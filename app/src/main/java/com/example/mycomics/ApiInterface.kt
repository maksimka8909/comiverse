package com.example.mycomics

import android.graphics.Bitmap
import android.service.autofill.UserData
import com.example.mycomics.dataclasses.UserAuthData
import com.example.mycomics.models.EmailCode
import com.example.mycomics.models.Genre
import com.example.mycomics.models.Message
import com.example.mycomics.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


public interface ApiInterface {

    @GET("api/user")
    fun getUsers(): Call<List<User>>

    @GET("api/genre")
    fun getGenres(): Call<List<Genre>>

    @Multipart
    @POST("api/user/reg")
    fun regUser(@Part avatar : MultipartBody.Part?,
                @Part("name")name : String,
                @Part("login")login : String,
                @Part("password")password : String,
                @Part("email")email: String): Call<Message>


    @POST("api/user/auth")
    fun authUser(@Body userAuth : UserAuthData): Call<User>

    @GET("api/User/UpdateLog")
    fun logUpdate(@Query("idUser")idUser: Int): Call<Message>

    @GET("api/User/CheckLogin")
    fun checkLogin(@Query("login")login: String): Call<Message>

    @GET("api/User/CheckEmail")
    fun checkEmail(@Query("email")email: String): Call<EmailCode>

   companion object RetrofitBuilder{
       fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://u1612286.plsk.regruhosting.ru/")
                .build()
            return retrofit.create(ApiInterface::class.java);
        }
    }
}