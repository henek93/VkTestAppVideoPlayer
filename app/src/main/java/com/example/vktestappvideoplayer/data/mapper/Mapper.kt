package com.example.vktestappvideoplayer.data.mapper

import com.example.vktestappvideoplayer.data.local.model.CachedVideoModel
import com.example.vktestappvideoplayer.data.network.dto.PexelsVideoDto
import com.example.vktestappvideoplayer.domain.entity.Video
import javax.inject.Inject

/**
 * Maps video data between different layers.
 */
class Mapper @Inject constructor() {
    /**
     * Converts a Pexels DTO to a domain Video entity.
     */
    fun pexelsToVideo(pexelsVideo: PexelsVideoDto): Video {
        return Video(
            id = pexelsVideo.id.toString(),
            thumbnailUrl = pexelsVideo.thumbnailUrl,
            videoUrl = pexelsVideo.videoFiles.firstOrNull()?.link ?: "",
            title = "This video was made by ${pexelsVideo.user.name}",
            duration = pexelsVideo.duration,
            authName = pexelsVideo.user.name
        )
    }

    /**
     * Converts a cached video to a domain Video entity.
     */
    fun cachedToVideo(cachedVideoModel: CachedVideoModel): Video {
        return Video(
            id = cachedVideoModel.id,
            thumbnailUrl = cachedVideoModel.thumbnailUrl,
            videoUrl = cachedVideoModel.videoUrl,
            title = cachedVideoModel.title,
            duration = cachedVideoModel.duration,
            authName = cachedVideoModel.authName
        )
    }
}