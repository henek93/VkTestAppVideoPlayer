package com.example.vktestappvideoplayer.data.mapper

import com.example.vktestappvideoplayer.data.local.model.CachedVideoModel
import com.example.vktestappvideoplayer.data.network.dto.PexelsVideoDto
import com.example.vktestappvideoplayer.domain.entity.Video

class Mapper {
    fun pexelsToVideo(pexelsVideo: PexelsVideoDto): Video {
        return Video(
            id = pexelsVideo.id.toString(),
            thumbnailUrl = pexelsVideo.thumbnailUrl,
            videoUrl = pexelsVideo.videoFiles.firstOrNull()?.link ?: "",
            title = "Video ${pexelsVideo.id}",
            duration = pexelsVideo.duration
        )
    }


    fun cachedToVideo(cachedVideoModel: CachedVideoModel): Video {
        return Video(
            id = cachedVideoModel.id,
            thumbnailUrl = cachedVideoModel.thumbnailUrl,
            videoUrl = cachedVideoModel.videoUrl,
            title = cachedVideoModel.title,
            duration = cachedVideoModel.duration
        )
    }
}