package com.example.vktestappvideoplayer.domain.repository

import com.example.vktestappvideoplayer.domain.entity.Video
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    fun getVideos(page: Int): Flow<List<Video>>
}