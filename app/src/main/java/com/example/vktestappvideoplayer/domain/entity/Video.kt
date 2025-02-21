package com.example.vktestappvideoplayer.domain.entity

data class Video(
    val id: Int,
    val thumbnailUrl: String,
    val videoUrl: String,
    val title: String,
    val duration: Int
)
