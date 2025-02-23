package com.example.vktestappvideoplayer.di

import androidx.lifecycle.ViewModel
import com.example.vktestappvideoplayer.presentation.videoList.VideoListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(VideoListViewModel::class)
    @Binds
    fun bindVideoListViewModel(viewModel: VideoListViewModel): ViewModel
}