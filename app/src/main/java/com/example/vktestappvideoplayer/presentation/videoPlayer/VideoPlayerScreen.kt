package com.example.vktestappvideoplayer.presentation.videoPlayer

import android.content.pm.ActivityInfo
import androidx.activity.compose.LocalActivity
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.vktestappvideoplayer.domain.entity.Video
import com.example.vktestappvideoplayer.presentation.main.MainViewModelFactory
import com.example.vktestappvideoplayer.presentation.videoList.VideoListScreen


@Composable
fun VideoPlayerScreen(
    video: Video,
    viewModelFactory: MainViewModelFactory,
    onVideoClick: (Video) -> Unit
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(video.videoUrl))
            prepare()
        }
    }

    // Освобождаем ресурсы плеера при уничтожении экрана
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Состояние полноэкранного режима
    var isFullScreen by remember { mutableStateOf(false) }
    val activity = LocalActivity.current

    // Управление ориентацией экрана
    LaunchedEffect(isFullScreen) {
        activity?.requestedOrientation = if (isFullScreen) {
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    Column {
        VideoContainer(
            exoPlayer = exoPlayer,
            video = video,
            isFullScreen = isFullScreen,
            onFullScreenToggle = { isFullScreen = !isFullScreen }
        )

        if (!isFullScreen) {
            VideoListScreen(
                paddingValues = PaddingValues(0.dp),
                viewModelFactory = viewModelFactory,
                onVideoClick = { onVideoClick(it) },
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
    onFullScreenToggle: () -> Unit
) {
    // Видеоплеер
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
                useController = true
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

                setOnClickListener {
                    onFullScreenToggle()
                }
            }
        },
        modifier = if (isFullScreen) {
            Modifier.fillMaxSize()
        } else {
            Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
        }
    )
}

@Composable
fun VideoContainer(
    video: Video,
    exoPlayer: ExoPlayer,
    isFullScreen: Boolean,
    onFullScreenToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(4.dp),
    ) {
        VideoPlayer(
            exoPlayer = exoPlayer,
            isFullScreen = isFullScreen,
            onFullScreenToggle = onFullScreenToggle
        )

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