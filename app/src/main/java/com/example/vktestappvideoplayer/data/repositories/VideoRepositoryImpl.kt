package com.example.vktestappvideoplayer.data.repositories

import android.content.Context
import android.util.Log
import com.example.vktestappvideoplayer.data.local.db.AppDatabase
import com.example.vktestappvideoplayer.data.local.db.VideoDao
import com.example.vktestappvideoplayer.data.local.model.CachedVideoModel
import com.example.vktestappvideoplayer.data.mapper.Mapper
import com.example.vktestappvideoplayer.data.network.api.ApiFactory
import com.example.vktestappvideoplayer.domain.entity.Video
import com.example.vktestappvideoplayer.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class VideoRepositoryImpl(
    context: Context
) : VideoRepository {


    private val apiService = ApiFactory.apiService
    private val videoDao: VideoDao = AppDatabase.getInstance(context).videoDao()
    private val mapper = Mapper()

    override fun getVideos(): Flow<List<Video>> = flow {
        try {
            val remoteVideos = apiService.getPopularVideos().videos.map { mapper.pexelsToVideo(it) }
            videoDao.insertAll(remoteVideos.map { it.toCachedModel() })
            emit(remoteVideos)
        } catch (e: Exception) {
            Log.d("ExceptionInVideoRepository", "Error fetching videos: ${e.message}")
            val cachedVideos = videoDao.getAll().map { cachedList ->
                cachedList.mapNotNull { cachedVideo ->
                    try {
                        mapper.cachedToVideo(cachedVideo)
                    } catch (mappingException: Exception) {
                        Log.e(
                            "MapperError",
                            "Failed to map cached video: ${mappingException.message}"
                        )
                        null
                    }
                }
            }
            emitAll(cachedVideos)
        }
    }

    private fun Video.toCachedModel(): CachedVideoModel {
        return CachedVideoModel(
            id = id,
            thumbnailUrl = thumbnailUrl,
            videoUrl = videoUrl,
            title = title,
            duration = duration
        )
    }
}
