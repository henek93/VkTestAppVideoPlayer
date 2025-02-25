package com.example.vktestappvideoplayer.domain.entity

/**
 * Represents a video entity in the domain layer.
 */
data class Video(
    val id: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val title: String,
    val duration: Int,
    val authName: String
)