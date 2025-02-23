package com.example.vktestappvideoplayer.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vktestappvideoplayer.presentation.videoList.VideoListViewModel

class MainViewModelFactory(
    val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideoListViewModel(context = context) as T
    }
}