package com.example.vktestappvideoplayer.presentation.videoPlayer

import android.content.Context
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.vktestappvideoplayer.data.local.db.VideoCache
import com.example.vktestappvideoplayer.domain.entity.Video
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Manages video playback state.
 */
class VideoPlayerViewModel : ViewModel() {
    private val _state = MutableStateFlow<VideoPlayerState>(VideoPlayerState.Loading)
    val state: StateFlow<VideoPlayerState> = _state
    var exoPlayer: ExoPlayer? = null
        private set

    @OptIn(UnstableApi::class)
    fun initializePlayer(context: Context, video: Video) {
        exoPlayer = ExoPlayer.Builder(context)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(
                    CacheDataSource.Factory()
                        .setCache(VideoCache.getCache(context))
                        .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context))
                )
            )
            .build()
            .apply {
                setMediaItem(MediaItem.fromUri(video.videoUrl))
                prepare()
                playWhenReady = true
                addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        _state.value = VideoPlayerState.Error(
                            message = error.errorCodeName ?: "Playback error",
                            showNoInternetMessage = error.errorCode == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED
                        )
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        _state.value = when (playbackState) {
                            Player.STATE_BUFFERING -> VideoPlayerState.Playing(isBuffering = true)
                            Player.STATE_READY -> VideoPlayerState.Playing(isBuffering = false)
                            else -> _state.value
                        }
                    }
                })
            }
    }

    fun toggleFullScreen(isFullScreen: Boolean) {
        val current = _state.value
        if (current is VideoPlayerState.Playing) _state.value =
            current.copy(isFullScreen = isFullScreen)
    }

    fun retry() {
        exoPlayer?.prepare()
        _state.value = VideoPlayerState.Playing(isBuffering = true)
    }

    @OptIn(UnstableApi::class)
    override fun onCleared() {
        exoPlayer?.release()
        VideoCache.releaseCache()
        exoPlayer = null
    }
}