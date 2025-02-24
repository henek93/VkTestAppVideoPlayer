package com.example.vktestappvideoplayer.presentation.videoPlayer

sealed class VideoPlayerState {
    object Loading : VideoPlayerState() // Загрузка плеера
    data class LoadingList(val isBuffering: Boolean = false) : VideoPlayerState() // Загрузка списка видео
    data class Playing(
        val isFullScreen: Boolean = false,
        val isBuffering: Boolean = false,
        val showNoInternetMessage: Boolean = false
    ) : VideoPlayerState()

    data class Error(
        val isFullScreen: Boolean = false,
        val isBuffering: Boolean = false,
        val showNoInternetMessage: Boolean = false,
        val message: String
    ) : VideoPlayerState()
}