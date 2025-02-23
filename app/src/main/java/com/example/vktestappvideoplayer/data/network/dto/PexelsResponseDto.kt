package com.example.vktestappvideoplayer.data.network.dto

import com.google.gson.annotations.SerializedName

data class PexelsResponseDto(
    @SerializedName("videos") val videos: List<PexelsVideoDto>
)