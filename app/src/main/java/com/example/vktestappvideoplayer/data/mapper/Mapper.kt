package com.example.vktestappvideoplayer.data.mapper

import com.example.vktestappvideoplayer.data.local.model.CachedVideoModel
import com.example.vktestappvideoplayer.data.network.model.VideoDto
import com.example.vktestappvideoplayer.domain.entity.Video
import javax.inject.Inject

class Mapper @Inject constructor() {

    fun cachedToVideo(cachedVideoModel: CachedVideoModel) = Video(
        id = cachedVideoModel.id,
        thumbnailUrl = cachedVideoModel.thumbnailUrl,
        videoUrl = cachedVideoModel.videoUrl,
        title = cachedVideoModel.title,
        duration = cachedVideoModel.duration
    )

    fun daoToVideo(videoDto: VideoDto) = Video(
        id = videoDto.id,
        thumbnailUrl = videoDto.image,
        videoUrl = videoDto.video_files.firstOrNull()?.link ?: "",
        title = "Video ${videoDto.id}",
        duration = videoDto.duration
    )

    fun videoToCached(video: Video) = CachedVideoModel(
        id = video.id,
        thumbnailUrl = video.thumbnailUrl,
        videoUrl = video.videoUrl,
        title = video.title,
        duration = video.duration
    )

    fun listCachedVideoToListVideo(list: List<CachedVideoModel>) = list.map {
        cachedToVideo(cachedVideoModel = it)
    }
}