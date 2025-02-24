package com.example.vktestappvideoplayer.presentation.videoList

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.vktestappvideoplayer.domain.entity.Video
import com.example.vktestappvideoplayer.domain.usecase.GetVideosUseCase
import com.example.vktestappvideoplayer.presentation.isNetworkAvailable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

class VideoListViewModel @Inject constructor(
    private val getVideosUseCase: GetVideosUseCase,
    private val context: Context
) : ViewModel() {
    private var currentPage = 1
    private val _screenState = MutableStateFlow<VideoListScreenState>(VideoListScreenState.Loading)
    val screenState: StateFlow<VideoListScreenState> = _screenState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val videos = MutableStateFlow<List<Video>>(emptyList())

    init {
        loadVideos()
    }

    fun loadVideos() {
        viewModelScope.launch {
            _screenState.value = VideoListScreenState.Loading
            try {
                if (!isNetworkAvailable(context = context)) {
                    _screenState.value = VideoListScreenState.Error("Нет подключения к интернету")
                    return@launch
                }
                getVideosUseCase(currentPage).collect { videos ->
                    if (videos.isEmpty()) {
                        _screenState.value =
                            VideoListScreenState.Error("Ошибка подключения к серверу. Попробуйте позже или подключить VPN.")
                    } else {
                        this@VideoListViewModel.videos.value += videos
                        _screenState.value =
                            VideoListScreenState.Success(this@VideoListViewModel.videos.value)
                    }
                }
            } catch (e: IOException) {
                _screenState.value = VideoListScreenState.Error("Нет подключения к интернету")
            } catch (e: HttpException) {
                if (e.response.code == 522) {
                    _screenState.value =
                        VideoListScreenState.Error("Сервер недоступен. Попробуйте позже или подключить VPN.")
                } else {
                    _screenState.value =
                        VideoListScreenState.Error("Ошибка сервера: ${e.response.code}")
                }
            } catch (e: Exception) {
                _screenState.value = VideoListScreenState.Error("Неизвестная ошибка: ${e.message}")
            }
        }
    }

    fun loadMoreVideos() {
        viewModelScope.launch {
            try {
                currentPage++
                getVideosUseCase(currentPage).collect {
                    if (it.isEmpty()) {
                        _screenState.value =
                            VideoListScreenState.Error("Ошибка подключения к серверу. Попробуйте позже или подключить VPN.")
                    } else {
                        videos.value += it
                        _screenState.value = VideoListScreenState.Success(videos.value)
                    }
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
                currentPage = 1
                getVideosUseCase(currentPage).collect {
                    if (it.isEmpty()) {
                        _screenState.value =
                            VideoListScreenState.Error("Ошибка подключения к серверу. Попробуйте позже или подключить VPN.")
                    } else {
                        videos.value = it
                        _screenState.value = VideoListScreenState.Success(videos.value)
                    }
                }
            } catch (e: Exception) {
                _screenState.value = VideoListScreenState.Error(e.message ?: "Unknown error")
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}