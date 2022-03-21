package com.example.mycomics.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id : Int,
    @SerializedName("login") val login : String,
    @SerializedName("password") val password : String,
    @SerializedName("name") val name : String,
    @SerializedName("photo") val photo : String,
    @SerializedName("lastLog") val lastLog : String,
    @SerializedName("email") val email : String,
    @SerializedName("access") val access : Boolean,
    @SerializedName("role") val role : Boolean
)
