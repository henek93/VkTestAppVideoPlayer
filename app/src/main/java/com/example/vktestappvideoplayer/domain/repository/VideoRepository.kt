package com.example.vktestappvideoplayer.domain.repository

import com.example.vktestappvideoplayer.domain.entity.Video
import kotlinx.coroutines.flow.Flow

/**
 * Defines the contract for retrieving video data.
 */
interface VideoRepository {
    /**
     * Fetches a list of videos for the specified page.
     */
    fun getVideos(page: Int): Flow<List<Video>>
}