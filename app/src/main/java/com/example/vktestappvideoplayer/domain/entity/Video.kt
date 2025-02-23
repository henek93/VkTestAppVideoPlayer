package com.example.vktestappvideoplayer.domain.entity

data class Video(
    val id: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val title: String,
    val duration: Int
)
