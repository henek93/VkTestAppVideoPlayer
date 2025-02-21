package com.example.vktestappvideoplayer.data.repositories

import com.example.vktestappvideoplayer.data.local.db.VideoDao
import com.example.vktestappvideoplayer.data.mapper.Mapper
import com.example.vktestappvideoplayer.data.network.api.PexelsApiService
import com.example.vktestappvideoplayer.domain.entity.Video
import com.example.vktestappvideoplayer.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val apiService: PexelsApiService,
    private val videoDao: VideoDao,
    private val mapper: Mapper
) : VideoRepository {

    override fun getVideos(): Flow<List<Video>> = flow {
        try {
            val remoteVideos = apiService.getPopularVideos().videos.map { mapper.daoToVideo(it) }
            videoDao.insertAll(remoteVideos.map { mapper.videoToCached(it) })
            emit(remoteVideos)
        } catch (e: Exception) {
            emitAll(videoDao.getAll().map { mapper.listCachedVideoToListVideo(list = it) })
        }
    }
}