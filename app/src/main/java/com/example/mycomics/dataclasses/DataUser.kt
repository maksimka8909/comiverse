package com.example.mycomics.dataclasses

import com.google.gson.annotations.SerializedName

data class DataUser(
    @SerializedName("login") val login : String,
    @SerializedName("password") val password : String,
    @SerializedName("name") val name : String,
    @SerializedName("avatar") val avatar : String,
    @SerializedName("email") val email : String
)
