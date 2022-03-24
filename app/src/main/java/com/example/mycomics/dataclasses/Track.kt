package com.example.mycomics.dataclasses

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("idComics") val idComics : Int,
    @SerializedName("idUser") val idUser : Int
)
