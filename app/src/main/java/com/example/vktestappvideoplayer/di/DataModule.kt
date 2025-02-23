package com.example.vktestappvideoplayer.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.example.vktestappvideoplayer.data.local.db.AppDatabase
import com.example.vktestappvideoplayer.data.local.db.VideoCache
import com.example.vktestappvideoplayer.data.local.db.VideoDao
import com.example.vktestappvideoplayer.data.network.api.ApiFactory
import com.example.vktestappvideoplayer.data.network.api.PexelsApiService
import com.example.vktestappvideoplayer.data.repositories.VideoRepositoryImpl
import com.example.vktestappvideoplayer.domain.repository.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: VideoRepositoryImpl): VideoRepository

    companion object {

        @ApplicationScope
        @Provides
        fun providesApiService(): PexelsApiService {
            return ApiFactory.apiService
        }

        @ApplicationScope
        @Provides
        fun provideDatabase(context: Context): AppDatabase {
            return AppDatabase.getInstance(context)
        }

        @ApplicationScope
        @Provides
        fun provideDao(database: AppDatabase): VideoDao {
            return database.videoDao()
        }

        @OptIn(UnstableApi::class)
        @ApplicationScope
        @Provides
        fun providesVideoCache(): VideoCache {
            return VideoCache
        }
    }
}