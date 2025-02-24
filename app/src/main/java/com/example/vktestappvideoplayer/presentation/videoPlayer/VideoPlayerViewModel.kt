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

@OptIn(UnstableApi::class)
class VideoPlayerViewModel : ViewModel() {

    private val _state = MutableStateFlow<VideoPlayerState>(VideoPlayerState.Loading)
    val state: StateFlow<VideoPlayerState> = _state

    var exoPlayer: ExoPlayer? = null


    fun initializePlayer(context: Context, video: Video) {
        val cache = VideoCache.getCache(context)
        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context))

        exoPlayer = ExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
            .build()
            .apply {
                setMediaItem(MediaItem.fromUri(video.videoUrl))
                prepare()
                playWhenReady = true

                addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        _state.value = VideoPlayerState.Error(
                            message = when (error.errorCode) {
                                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> "Ошибка сети. Проверьте подключение к интернету."
                                PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS -> "Ошибка сервера: ${error.errorCode}"
                                else -> "Ошибка воспроизведения: ${error.message}"
                            },
                            isBuffering = false,
                            showNoInternetMessage = error.errorCode == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED
                        )
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_BUFFERING -> {
                                _state.value = VideoPlayerState.Playing(isBuffering = true)
                            }

                            Player.STATE_READY -> {
                                _state.value = VideoPlayerState.Playing(isBuffering = false)
                            }
                        }
                    }
                })
            }
    }

    fun toggleFullScreen(isFullScreen: Boolean) {
        val currentState = _state.value
        if (currentState is VideoPlayerState.Playing) {
            _state.value = currentState.copy(isFullScreen = isFullScreen)
        }

    }

    fun retry() {
        exoPlayer?.prepare()
        _state.value = VideoPlayerState.Playing(isBuffering = true)
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer?.release()
        exoPlayer = null
    }
}