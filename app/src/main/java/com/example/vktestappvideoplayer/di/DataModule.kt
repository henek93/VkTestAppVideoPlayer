package com.example.vktestappvideoplayer.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
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

/**
 * Provides dependencies for the data layer.
 */
@Module
interface DataModule {
    /**
     * Binds the VideoRepository implementation to its interface.
     */
    @ApplicationScope
    @Binds
    fun bindRepository(impl: VideoRepositoryImpl): VideoRepository

    companion object {
        /**
         * Provides the Pexels API service instance.
         */
        @ApplicationScope
        @Provides
        fun provideApiService(): PexelsApiService {
            return ApiFactory.apiService
        }

        /**
         * Provides the Room database instance.
         */
        @ApplicationScope
        @Provides
        fun provideDatabase(context: Context): AppDatabase {
            return AppDatabase.getInstance(context)
        }

        /**
         * Provides the VideoDao from the database.
         */
        @ApplicationScope
        @Provides
        fun provideDao(database: AppDatabase): VideoDao {
            return database.videoDao()
        }

        /**
         * Provides the VideoCache instance (singleton object).
         */
        @OptIn(UnstableApi::class)
        @ApplicationScope
        @Provides
        fun provideVideoCache(context: Context): Cache { // Изменен тип на Cache
            return VideoCache.getCache(context)
        }
    }
}