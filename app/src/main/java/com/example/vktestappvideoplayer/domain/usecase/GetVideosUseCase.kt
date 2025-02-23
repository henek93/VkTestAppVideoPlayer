package com.example.vktestappvideoplayer.domain.usecase

import com.example.vktestappvideoplayer.domain.entity.Video
import com.example.vktestappvideoplayer.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(
    private val repository: VideoRepository
) {
    operator fun invoke(page: Int): Flow<List<Video>> {
        return repository.getVideos(page)
    }
}