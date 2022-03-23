package com.example.mycomics.models

import com.google.gson.annotations.SerializedName

data class Comics(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("cover") val cover : String,
    @SerializedName("date") val date : String,
    @SerializedName("description") val description : String,
    @SerializedName("author") val author : String,
    @SerializedName("editor") val editor : String
)
