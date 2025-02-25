package com.example.vktestappvideoplayer.presentation.videoList

import com.example.vktestappvideoplayer.domain.entity.Video

/**
 * Represents states of the video list screen.
 */
sealed class VideoListScreenState {
    object Loading : VideoListScreenState()
    data class Success(val videos: List<Video>, val isRefreshing: Boolean = false) : VideoListScreenState()
    data class Error(val message: String) : VideoListScreenState()
    object Refreshing : VideoListScreenState()
}