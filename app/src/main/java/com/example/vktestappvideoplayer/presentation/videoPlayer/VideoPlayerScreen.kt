package com.example.vktestappvideoplayer.presentation.videoPlayer

import android.content.res.Configuration
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.vktestappvideoplayer.data.local.db.VideoCache
import com.example.vktestappvideoplayer.domain.entity.Video
import com.example.vktestappvideoplayer.presentation.main.MainViewModelFactory
import com.example.vktestappvideoplayer.presentation.videoList.VideoListScreen

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerScreen(
    video: Video,
    viewModelFactory: MainViewModelFactory,
    onVideoClick: (Video) -> Unit
) {
    var playbackPosition by rememberSaveable { mutableStateOf(0L) }
    var isPlaying by rememberSaveable { mutableStateOf(true) } // Состояние воспроизведения
    var isFullScreen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) } // Сообщение об ошибке
    val context = LocalContext.current

    val cache = remember {
        VideoCache.getCache(context)
    }

    // Инициализация ExoPlayer с кэшированием
    val exoPlayer = remember {
        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context))

        ExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
            .build()
            .apply {
                setMediaItem(MediaItem.fromUri(video.videoUrl))
                prepare()
                playWhenReady = isPlaying
                seekTo(playbackPosition)
                addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        errorMessage = "Ошибка воспроизведения: ${error.message}"
                    }
                })
            }
    }

    // Освобождаем ресурсы плеера при уничтожении экрана
    DisposableEffect(Unit) {
        onDispose {
            playbackPosition = exoPlayer.currentPosition
            isPlaying = exoPlayer.isPlaying
            exoPlayer.release()
        }
    }

    // Следим за изменением ориентации экрана
    val configuration = LocalConfiguration.current
    LaunchedEffect(configuration.orientation) {
        isFullScreen = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    Column {
        // Отображение ошибки, если она есть
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }

        VideoContainer(
            exoPlayer = exoPlayer,
            video = video,
            isFullScreen = isFullScreen,
            onFullScreenToggle = { isFullScreen = !isFullScreen },
        )

        if (!isFullScreen) {
            VideoListScreen(
                paddingValues = PaddingValues(0.dp),
                viewModelFactory = viewModelFactory,
                onVideoClick = {
                    onVideoClick(it)
                },
                video = video
            )
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    exoPlayer: ExoPlayer,
    isFullScreen: Boolean,
    onFullScreenToggle: () -> Unit,
) {
    Box(
        modifier = if (isFullScreen) {
            Modifier.fillMaxSize()
        } else {
            Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
        }
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = true
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                }
            },
            modifier = Modifier.matchParentSize()
        )

        // Продолжительность видео
        Box(
            modifier = Modifier
                .size(35.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                .align(Alignment.TopEnd)
                .padding(4.dp)
        ) {
            IconButton(
                onClick = onFullScreenToggle,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(2.dp)
            ) {
                Icon(
                    imageVector = if (isFullScreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                    contentDescription = if (isFullScreen) "Exit Fullscreen" else "Fullscreen",
                    tint = Color.White
                )
            }
        }

    }
}


@Composable
fun VideoContainer(
    video: Video,
    exoPlayer: ExoPlayer,
    isFullScreen: Boolean,
    onFullScreenToggle: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(4.dp),
    ) {
        // Анимация перехода между режимами
        AnimatedContent(
            targetState = isFullScreen,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }
        ) { fullScreen ->
            if (fullScreen) {
                VideoPlayer(
                    exoPlayer = exoPlayer,
                    isFullScreen = true,
                    onFullScreenToggle = onFullScreenToggle,
                )
            } else {
                VideoPlayer(
                    exoPlayer = exoPlayer,
                    isFullScreen = false,
                    onFullScreenToggle = onFullScreenToggle,
                )
            }
        }

        if (!isFullScreen) {
            Spacer(modifier = Modifier.height(6.dp))

            // Заголовок видео
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = video.title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
                text = video.authName,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}