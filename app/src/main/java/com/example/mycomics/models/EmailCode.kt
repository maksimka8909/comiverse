package com.example.mycomics.models

import com.google.gson.annotations.SerializedName

data class EmailCode (
    @SerializedName("key") val key : String
)