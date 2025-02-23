package com.example.vktestappvideoplayer.presentation.videoList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vktestappvideoplayer.domain.entity.Video
import com.example.vktestappvideoplayer.domain.usecase.GetVideosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoListViewModel @Inject constructor(
    private val getVideosUseCase: GetVideosUseCase
) : ViewModel() {
    private var currentPage = 1
    private val _screenState = MutableStateFlow<VideoListScreenState>(VideoListScreenState.Loading)
    val screenState: StateFlow<VideoListScreenState> = _screenState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos: StateFlow<List<Video>> = _videos

    init {
        loadVideos()
    }

    fun loadVideos() {
        viewModelScope.launch {
            _screenState.value = VideoListScreenState.Loading
            try {
                getVideosUseCase(currentPage).collect {
                    _videos.value += it // Добавляем новые видео к существующему списку
                    _screenState.value = VideoListScreenState.Success(_videos.value)
                }

            } catch (e: Exception) {
                _screenState.value = VideoListScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadMoreVideos() {
        viewModelScope.launch {
            try {
                currentPage++
                getVideosUseCase(currentPage).collect {
                    _videos.value += it // Добавляем новые видео к существующему списку
                    _screenState.value = VideoListScreenState.Success(_videos.value)
                }
            } catch (e: Exception) {
                _screenState.value = VideoListScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun refreshVideos() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _screenState.value = VideoListScreenState.Refreshing
            try {
                currentPage = 1 // Сбрасываем страницу
                getVideosUseCase(currentPage).collect {
                    _videos.value = it // Обновляем список видео
                    _screenState.value = VideoListScreenState.Success(_videos.value)
                }

            } catch (e: Exception) {
                _screenState.value = VideoListScreenState.Error(e.message ?: "Unknown error")
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}