package com.example.vktestappvideoplayer.data.network.dto

import com.google.gson.annotations.SerializedName

data class PexelsVideoDto(
    @SerializedName("id") val id: Int,
    @SerializedName("image") val thumbnailUrl: String,
    @SerializedName("video_files") val videoFiles: List<PexelsVideoFileDto>,
    @SerializedName("duration") val duration: Int,
    @SerializedName("user") val user: UserDto
)