package com.example.vktestappvideoplayer.presentation.videoPlayer

import android.content.res.Configuration
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.vktestappvideoplayer.domain.entity.Video
import com.example.vktestappvideoplayer.presentation.videoList.VideoListScreen
import com.example.vktestappvideoplayer.ui.theme.Constants

@Composable
fun VideoPlayerScreen(
    video: Video,
    onVideoClick: (Video) -> Unit
) {
    val context = LocalContext.current
    val viewModel: VideoPlayerViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    // Определяем ориентацию экрана (ландшафтная или портретная)
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Автоматически переключаемся в полноэкранный режим при изменении ориентации
    LaunchedEffect(isLandscape) {
        viewModel.toggleFullScreen(isLandscape)
    }

    // Инициализация плеера при первом запуске экрана
    LaunchedEffect(Unit) {
        viewModel.initializePlayer(context, video)
    }

    // Отображение состояния экрана в зависимости от текущего состояния ViewModel
    Box(modifier = Modifier.fillMaxSize()) {
        when (val currentState = state) {
            is VideoPlayerState.Loading -> RenderLoadingState(viewModel, video, isLandscape)
            is VideoPlayerState.LoadingList -> RenderLoadingListState(
                exoPlayer = viewModel.exoPlayer!!,
                video = video,
                isLandscape = isLandscape,
                currentState = currentState,
                onFullScreenToggle = { viewModel.toggleFullScreen(it) },
                onRetry = { viewModel.retry() }
            )

            is VideoPlayerState.Playing -> RenderPlayingState(
                viewModel,
                video,
                isLandscape,
                currentState,
                onVideoClick
            )

            is VideoPlayerState.Error -> RenderErrorState(
                viewModel,
                video,
                isLandscape,
                currentState,
                onVideoClick
            )
        }
    }
}

@Composable
private fun RenderLoadingState(
    viewModel: VideoPlayerViewModel,
    video: Video,
    isLandscape: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Если плеер инициализирован, отображаем контейнер с видео
        if (viewModel.exoPlayer != null) {
            VideoContainer(
                exoPlayer = viewModel.exoPlayer!!,
                video = video,
                isFullScreen = isLandscape,
                onFullScreenToggle = { viewModel.toggleFullScreen(!isLandscape) },
                isLoading = true,
                showNoInternetMessage = false,
                onRetry = { viewModel.retry() }
            )
        } else {
            // Если плеер не инициализирован, показываем индикатор загрузки
            RenderLoadingIndicatorPlayer()
        }

        // В портретной ориентации показываем дополнительный индикатор загрузки
        if (!isLandscape) {
            RenderLoadingIndicator()
        }
    }
}

@Composable
private fun RenderLoadingListState(
    exoPlayer: ExoPlayer,
    video: Video,
    isLandscape: Boolean,
    currentState: VideoPlayerState.LoadingList,
    onRetry: () -> Unit,
    onFullScreenToggle: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Отображаем контейнер с видео
        VideoContainer(
            exoPlayer = exoPlayer,
            video = video,
            isFullScreen = isLandscape,
            onFullScreenToggle = { onFullScreenToggle(isLandscape) },
            isLoading = currentState.isBuffering,
            showNoInternetMessage = false,
            onRetry = { onRetry() }
        )

        // В портретной ориентации показываем индикатор загрузки
        if (!isLandscape) {
            RenderLoadingIndicator()
        }
    }
}

@Composable
private fun RenderPlayingState(
    viewModel: VideoPlayerViewModel,
    video: Video,
    isLandscape: Boolean,
    currentState: VideoPlayerState.Playing,
    onVideoClick: (Video) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Отображаем контейнер с видео
        VideoContainer(
            exoPlayer = viewModel.exoPlayer!!,
            video = video,
            isFullScreen = currentState.isFullScreen || isLandscape,
            onFullScreenToggle = { viewModel.toggleFullScreen(!currentState.isFullScreen) },
            isLoading = currentState.isBuffering,
            showNoInternetMessage = currentState.showNoInternetMessage,
            onRetry = { viewModel.retry() }
        )

        // В портретной ориентации показываем список видео
        if (!currentState.isFullScreen && !isLandscape) {
            VideoListScreen(
                paddingValues = PaddingValues(0.dp),
                onVideoClick = { onVideoClick(it) },
                video = video
            )
        }
    }
}

@Composable
private fun RenderErrorState(
    viewModel: VideoPlayerViewModel,
    video: Video,
    isLandscape: Boolean,
    currentState: VideoPlayerState.Error,
    onVideoClick: (Video) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Отображаем контейнер с видео
        VideoContainer(
            exoPlayer = viewModel.exoPlayer!!,
            video = video,
            isFullScreen = currentState.isFullScreen || isLandscape,
            onFullScreenToggle = { viewModel.toggleFullScreen(!currentState.isFullScreen) },
            isLoading = currentState.isBuffering,
            showNoInternetMessage = currentState.showNoInternetMessage,
            onRetry = { viewModel.retry() }
        )

        // В портретной ориентации показываем список видео
        if (!currentState.isFullScreen && !isLandscape) {
            VideoListScreen(
                paddingValues = PaddingValues(0.dp),
                onVideoClick = { onVideoClick(it) },
                video = video
            )
        }
    }
}

@Composable
private fun ColumnScope.RenderLoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        // Отображаем круговой индикатор загрузки
        CircularProgressIndicator()
    }
}

@Composable
private fun RenderLoadingIndicatorPlayer() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(Constants.VIDEO_ASPECT_RATIO)
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Отображаем круговой индикатор загрузки с цветом из темы
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}


@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    exoPlayer: ExoPlayer,
    isFullScreen: Boolean,
    onFullScreenToggle: () -> Unit,
    isLoading: Boolean,
    showNoInternetMessage: Boolean
) {
    Box(
        modifier = if (isFullScreen) {
            Modifier.fillMaxSize() // В полноэкранном режиме занимаем весь экран
        } else {
            Modifier
                .fillMaxWidth()
                .aspectRatio(Constants.VIDEO_ASPECT_RATIO) // В обычном режиме сохраняем соотношение сторон
        }
    ) {
        // Используем AndroidView для встраивания PlayerView
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

        // Если идет загрузка, показываем индикатор
        if (isLoading) {
            LoadingState(
                showNoInternetMessage = showNoInternetMessage,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Кнопка переключения полноэкранного режима
        FullscreenButton(
            isFullScreen = isFullScreen,
            onFullScreenToggle = onFullScreenToggle,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )
    }
}

@Composable
fun VideoContainer(
    exoPlayer: ExoPlayer,
    video: Video,
    isFullScreen: Boolean,
    onFullScreenToggle: () -> Unit,
    isLoading: Boolean,
    showNoInternetMessage: Boolean,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.padding(4.dp),
    ) {
        // Анимация переключения полноэкранного режима
        AnimatedContent(
            targetState = isFullScreen,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }
        ) { fullScreen ->
            Box {
                VideoPlayer(
                    exoPlayer = exoPlayer,
                    isFullScreen = fullScreen,
                    onFullScreenToggle = onFullScreenToggle,
                    isLoading = isLoading,
                    showNoInternetMessage = showNoInternetMessage
                )

                // Если нет интернета, показываем сообщение об ошибке
                if (showNoInternetMessage) {
                    ErrorOverlay(
                        message = "Ошибка сети. Проверьте подключение к интернету.",
                        onRetry = onRetry
                    )
                }
            }
        }

        // В обычном режиме показываем информацию о видео
        if (!isFullScreen) {
            Spacer(modifier = Modifier.height(6.dp))
            VideoInfo(video)
        }
    }
}

@Composable
private fun VideoInfo(video: Video) {
    Column {
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = video.title,
            style = MaterialTheme.typography.titleMedium,
            fontSize = Constants.VIDEO_TITLE_TEXT_SIZE,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
            text = video.authName,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = Constants.VIDEO_AUTHOR_TEXT_SIZE
        )
    }
}

@Composable
private fun ErrorOverlay(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Сообщение об ошибке
            Text(
                text = message,
                color = Color.White,
                fontSize = Constants.ERROR_MESSAGE_TEXT_SIZE,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // Кнопка для повторной попытки
            Button(onClick = onRetry) {
                Text(text = "Повторить попытку")
            }
        }
    }
}

@Composable
private fun LoadingState(
    showNoInternetMessage: Boolean,
    modifier: Modifier = Modifier
) {
    if (showNoInternetMessage) {
        // Сообщение о проверке интернета
        Box(
            modifier = modifier
                .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                .padding(Constants.ERROR_MESSAGE_PADDING),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Проверьте наличие интернета",
                color = Color.White,
                fontSize = Constants.ERROR_MESSAGE_TEXT_SIZE
            )
        }
    } else {
        // Индикатор загрузки
        CircularProgressIndicator(
            modifier = modifier
                .size(Constants.LOADING_INDICATOR_SIZE),
            color = Color.White
        )
    }
}

@Composable
private fun FullscreenButton(
    isFullScreen: Boolean,
    onFullScreenToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(Constants.FULLSCREEN_ICON_SIZE)
            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
            .padding(Constants.FULLSCREEN_ICON_PADDING)
    ) {
        // Кнопка переключения полноэкранного режима
        IconButton(
            onClick = onFullScreenToggle,
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Icon(
                imageVector = if (isFullScreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                contentDescription = if (isFullScreen) "Exit Fullscreen" else "Fullscreen",
                tint = Color.White
            )
        }
    }
}