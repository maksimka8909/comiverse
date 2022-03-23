package com.example.mycomics.models

import com.google.gson.annotations.SerializedName

data class Issue(
    @SerializedName("idIssue") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("nameFile") val nameFile : String,
    @SerializedName("pathRead") val pathRead : String,
    @SerializedName("pathDownload") val pathDownload : String,
    @SerializedName("date") val date : String
)
