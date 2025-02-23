package com.example.vktestappvideoplayer.di

import com.example.vktestappvideoplayer.presentation.main.ViewModelFactory
import dagger.Module
import dagger.Subcomponent

@Module
interface VideoListScreenComponent {

    fun getViewModelFactory(): ViewModelFactory
}