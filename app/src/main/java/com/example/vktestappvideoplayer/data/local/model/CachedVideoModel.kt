package com.example.vktestappvideoplayer.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a video stored in the local database.
 */
@Entity(tableName = "videos")
data class CachedVideoModel(
    @PrimaryKey val id: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val title: String,
    val duration: Int,
    val authName: String
)