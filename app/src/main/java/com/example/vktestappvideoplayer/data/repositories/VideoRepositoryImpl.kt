package com.example.vktestappvideoplayer.data.repositories

import android.util.Log
import coil.network.HttpException
import com.example.vktestappvideoplayer.data.local.db.VideoDao
import com.example.vktestappvideoplayer.data.local.model.CachedVideoModel
import com.example.vktestappvideoplayer.data.mapper.Mapper
import com.example.vktestappvideoplayer.data.network.api.PexelsApiService
import com.example.vktestappvideoplayer.domain.entity.Video
import com.example.vktestappvideoplayer.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

/**
 * Implements video data retrieval from network and cache.
 */
class VideoRepositoryImpl @Inject constructor(
    private val apiService: PexelsApiService,
    private val videoDao: VideoDao,
    private val mapper: Mapper,
) : VideoRepository {

    /**
     * Fetches videos from the network or cache, emitting them as a Flow.
     */
    override fun getVideos(page: Int): Flow<List<Video>> = flow {
        try {
            val remoteVideos = apiService.getPopularVideos(page = page)
                .videos.map { mapper.pexelsToVideo(it) }
            videoDao.insertAll(remoteVideos.map { it.toCachedModel() })
            emit(remoteVideos)
        } catch (e: IOException) {
            Log.e("NetworkError", "No internet: ${e.message}")
            emitAll(videoDao.getAll().map { it.map { mapper.cachedToVideo(it) } })
        } catch (e: HttpException) {
            Log.e("ApiError", "HTTP error ${e.response.code}: ${e.message}")
            emit(emptyList())
        } catch (e: Exception) {
            Log.e("UnknownError", "Unexpected: ${e.message}")
            emit(emptyList())
        }
    }

    /**
     * Converts a domain Video to a cached model.
     */
    private fun Video.toCachedModel(): CachedVideoModel {
        return CachedVideoModel(
            id = id,
            thumbnailUrl = thumbnailUrl,
            videoUrl = videoUrl,
            title = title,
            duration = duration,
            authName = authName
        )
    }
}