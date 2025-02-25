package com.example.vktestappvideoplayer.di

import androidx.lifecycle.ViewModel
import com.example.vktestappvideoplayer.presentation.videoList.VideoListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Provides ViewModel bindings for the presentation layer.
 */
@Module
interface ViewModelModule {
    /**
     * Binds VideoListViewModel to the ViewModel map.
     */
    @IntoMap
    @ViewModelKey(VideoListViewModel::class)
    @Binds
    fun bindVideoListViewModel(viewModel: VideoListViewModel): ViewModel
}