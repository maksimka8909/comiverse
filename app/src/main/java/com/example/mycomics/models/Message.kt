package com.example.mycomics.models

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("message") val message : String
)
