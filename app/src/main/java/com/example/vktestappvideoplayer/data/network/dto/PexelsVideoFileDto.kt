package com.example.vktestappvideoplayer.data.network.dto

import com.google.gson.annotations.SerializedName

data class PexelsVideoFileDto(
    @SerializedName("link") val link: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
)