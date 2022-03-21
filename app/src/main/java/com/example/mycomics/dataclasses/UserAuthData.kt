package com.example.mycomics.dataclasses

import com.google.gson.annotations.SerializedName

data class UserAuthData(
    @SerializedName("login") val login : String,
    @SerializedName("password") val password : String
)
