package com.example.mycomics.models

import com.google.gson.annotations.SerializedName

data class Score(
    @SerializedName("result") val score : Int
)
