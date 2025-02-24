package com.example.vktestappvideoplayer.presentation.videoList

import com.example.vktestappvideoplayer.domain.entity.Video

sealed class VideoListScreenState {


    object Loading : VideoListScreenState()

    data class Success(
        val videos: List<Video>) :
        VideoListScreenState()

    data class Error(val message: String) : VideoListScreenState()
    object Refreshing : VideoListScreenState()

}