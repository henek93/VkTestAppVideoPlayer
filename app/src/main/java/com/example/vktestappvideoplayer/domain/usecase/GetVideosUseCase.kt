package com.example.vktestappvideoplayer.domain.usecase

import com.example.vktestappvideoplayer.domain.entity.Video
import com.example.vktestappvideoplayer.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving videos from the repository.
 */
class GetVideosUseCase @Inject constructor(
    private val repository: VideoRepository
) {
    /**
     * Executes the use case to fetch videos for the given page.
     */
    operator fun invoke(page: Int): Flow<List<Video>> {
        return repository.getVideos(page)
    }
}