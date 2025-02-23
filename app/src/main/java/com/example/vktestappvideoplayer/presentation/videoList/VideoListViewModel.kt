package com.example.vktestappvideoplayer.presentation.videoList

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vktestappvideoplayer.data.repositories.VideoRepositoryImpl
import com.example.vktestappvideoplayer.domain.entity.Video
import com.example.vktestappvideoplayer.domain.usecase.GetVideosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VideoListViewModel(context: Context) : ViewModel() {

    private val repository = VideoRepositoryImpl(context = context)
    private val getVideosUseCase = GetVideosUseCase(repository)
    private val API_KEY = "AIzaSyBCRnPxa8ouuJRm70gZUZ7d7-mpyI5q0qc"

    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos: StateFlow<List<Video>> = _videos

    init {
        viewModelScope.launch {
            getVideosUseCase().collect { videos ->
                _videos.value = videos
            }
        }
    }

    fun refreshVideos() {
        viewModelScope.launch {
            getVideosUseCase().collect { videos ->
                _videos.value = videos
            }
        }
    }
}