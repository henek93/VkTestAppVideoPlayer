package com.example.vktestappvideoplayer.presentation.videoList

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vktestappvideoplayer.domain.entity.Video
import com.example.vktestappvideoplayer.domain.usecase.GetVideosUseCase
import com.example.vktestappvideoplayer.presentation.isNetworkAvailable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Manages video list data and state.
 */
class VideoListViewModel @Inject constructor(
    private val getVideosUseCase: GetVideosUseCase,
    private val context: Context
) : ViewModel() {
    private var currentPage = 1
    private val _screenState = MutableStateFlow<VideoListScreenState>(VideoListScreenState.Loading)
    val screenState: StateFlow<VideoListScreenState> = _screenState
    private val videos = mutableListOf<Video>()

    init {
        loadVideos()
    }

    fun loadVideos() = viewModelScope.launch {
        _screenState.value = VideoListScreenState.Loading
        fetchVideos()
    }

    fun loadMoreVideos() = viewModelScope.launch {
        currentPage++
        fetchVideos()
    }

    fun refreshVideos() = viewModelScope.launch {
        _screenState.value = VideoListScreenState.Refreshing
        currentPage = 1
        videos.clear()
        fetchVideos()
    }

    private suspend fun fetchVideos() {
        if (!isNetworkAvailable(context)) {
            _screenState.value = VideoListScreenState.Error("No internet connection")
            return
        }
        getVideosUseCase(currentPage)
            .catch { handleError(it) }
            .collect { newVideos ->
                if (newVideos.isEmpty() && currentPage == 1) {
                    _screenState.value = VideoListScreenState.Error("Server unavailable")
                } else {
                    videos.addAll(newVideos)
                    _screenState.value = VideoListScreenState.Success(videos.toList())
                }
            }
    }

    private fun handleError(e: Throwable) {
        _screenState.value = when (e) {
            is java.io.IOException -> VideoListScreenState.Error("No internet")
            is coil.network.HttpException -> VideoListScreenState.Error("Server error: ${e.response.code}")
            else -> VideoListScreenState.Error("Unknown error")
        }
    }
}