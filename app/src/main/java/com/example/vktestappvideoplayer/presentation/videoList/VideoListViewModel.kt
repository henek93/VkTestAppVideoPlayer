package com.example.vktestappvideoplayer.presentation.videoList

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vktestappvideoplayer.data.repositories.VideoRepositoryImpl
import com.example.vktestappvideoplayer.domain.usecase.GetVideosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class VideoListViewModel(context: Context) : ViewModel() {


    private val repository = VideoRepositoryImpl(context = context)
    private val getVideosUseCase = GetVideosUseCase(repository)
    private var currentPage = 1

    private val _screenState = MutableStateFlow<VideoListScreenState>(VideoListScreenState.Loading)
    val screenState: StateFlow<VideoListScreenState> = _screenState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        loadVideos()
    }

    fun loadVideos() {
        viewModelScope.launch {
            _screenState.value = VideoListScreenState.Loading
            try {
                getVideosUseCase(currentPage).collect{
                    _screenState.value = VideoListScreenState.Success(it)
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
                currentPage++
                getVideosUseCase(currentPage).collect{
                    _screenState.value = VideoListScreenState.Success(it)
                }
            } catch (e: Exception) {
                _screenState.value = VideoListScreenState.Error(e.message ?: "Unknown error")
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}