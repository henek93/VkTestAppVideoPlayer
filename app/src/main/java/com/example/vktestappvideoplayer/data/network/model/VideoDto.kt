package com.example.vktestappvideoplayer.data.network.model

data class VideoDto(
    val id: Int,
    val width: Int,
    val height: Int,
    val url: String,
    val image: String, // Thumbnail URL
    val duration: Int, // Duration in seconds
    val video_files: List<VideoFileDto>
)