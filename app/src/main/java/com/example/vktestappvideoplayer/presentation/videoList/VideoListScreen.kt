package com.example.vktestappvideoplayer.presentation.videoList

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.vktestappvideoplayer.domain.entity.Video
import com.example.vktestappvideoplayer.presentation.getApplicationComponent
import com.example.vktestappvideoplayer.ui.theme.Constants

@Composable
fun VideoListScreen(
    paddingValues: PaddingValues,
    onVideoClick: (Video) -> Unit,
    video: Video? = null
) {
    val component = getApplicationComponent()
    val viewModel: VideoListViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState by viewModel.screenState.collectAsState()

    val configuration = LocalConfiguration.current
    val isLandscape = remember { configuration.orientation == Configuration.ORIENTATION_LANDSCAPE }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (val state = screenState) {
            is VideoListScreenState.Loading, is VideoListScreenState.Refreshing -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is VideoListScreenState.Success -> {
                VideoList(
                    paddingValues = paddingValues,
                    videos = state.videos,
                    onVideoClick = onVideoClick,
                    isRefreshing = state.isRefreshing,
                    onRefresh = { viewModel.refreshVideos() },
                    onLoadMore = { viewModel.loadMoreVideos() },
                    video = video,
                    isLandScape = isLandscape
                )
            }

            is VideoListScreenState.Error -> {
                ErrorState(
                    message = state.message,
                    onRetry = { viewModel.loadVideos() }
                )
            }
        }
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = Color.Red,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        Button(onClick = onRetry) {
            Text(text = "Повторить попытку")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoList(
    paddingValues: PaddingValues,
    isLandScape: Boolean,
    videos: List<Video>,
    onVideoClick: (Video) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    video: Video?
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty() && visibleItems.last().index >= videos.size - 1) {
                    onLoadMore()
                }
            }
    }

    PullToRefreshBox(
        modifier = Modifier.padding(paddingValues),
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
    ) {
        LazyColumn(state = lazyListState) {
            items(videos) { currentVideo ->
                if (video?.id != currentVideo.id) {
                    VideoItem(
                        isLandscape = isLandScape,
                        video = currentVideo,
                        onVideoClick = onVideoClick
                    )
                }
            }
        }
    }
}

@Composable
fun VideoItem(
    video: Video,
    onVideoClick: (Video) -> Unit,
    isLandscape: Boolean
) {
    var isImageLoaded by remember { mutableStateOf(false) }

    if (isLandscape) {
        LandscapeVideoItem(video, onVideoClick, isImageLoaded) { isImageLoaded = true }
    } else {
        PortraitVideoItem(video, onVideoClick, isImageLoaded) { isImageLoaded = true }
    }
}

@Composable
private fun LandscapeVideoItem(
    video: Video,
    onVideoClick: (Video) -> Unit,
    isImageLoaded: Boolean,
    onImageLoaded: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onVideoClick(video) })
            .padding(vertical = 4.dp),
    ) {
        VideoThumbnail(video, isImageLoaded, onImageLoaded, Modifier.weight(1f))
        VideoDescription(video, Modifier.weight(2f))
    }
}

@Composable
private fun PortraitVideoItem(
    video: Video,
    onVideoClick: (Video) -> Unit,
    isImageLoaded: Boolean,
    onImageLoaded: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onVideoClick(video) })
            .padding(vertical = 4.dp)
    ) {
        VideoThumbnail(
            video,
            isImageLoaded,
            onImageLoaded,
            Modifier
                .fillMaxWidth()
                .height(Constants.VIDEO_THUMBNAIL_HEIGHT)
        )
        VideoTitle(video)
        VideoAuthor(video)
    }
}

@Composable
private fun VideoThumbnail(
    video: Video,
    isImageLoaded: Boolean,
    onImageLoaded: () -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier.clip(RoundedCornerShape(8.dp))) {
        if (!isImageLoaded) {
            ShimmerEffect(modifier = Modifier.matchParentSize())
        }

        AsyncImage(
            model = video.thumbnailUrl,
            contentDescription = "Video Thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            onSuccess = { onImageLoaded() }
        )

        VideoDuration(video.duration)
    }
}

@Composable
private fun BoxScope.VideoDuration(duration: Int) {
    Box(
        modifier = Modifier
            .background(
                Color.Black.copy(alpha = 0.6f),
                RoundedCornerShape(Constants.VIDEO_DURATION_CORNER_RADIUS)
            )
            .align(Alignment.BottomEnd)
            .padding(horizontal = Constants.VIDEO_DURATION_PADDING, vertical = 4.dp)
    ) {
        Text(
            text = "${duration / 60}:${duration % 60}",
            color = Color.White,
            fontSize = Constants.VIDEO_DURATION_TEXT_SIZE,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun VideoDescription(video: Video, modifier: Modifier) {
    Column(modifier = modifier.padding(start = 8.dp)) {
        VideoTitle(video)
        Spacer(modifier = Modifier.height(4.dp))
        VideoAuthor(video)
    }
}

@Composable
private fun VideoTitle(video: Video) {
    Text(
        text = video.title,
        style = MaterialTheme.typography.titleMedium,
        fontSize = Constants.VIDEO_TITLE_TEXT_SIZE,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun VideoAuthor(video: Video) {
    Text(
        text = video.authName,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.secondary,
        fontSize = Constants.VIDEO_AUTHOR_TEXT_SIZE
    )
}

@Composable
fun ShimmerEffect(modifier: Modifier = Modifier) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = 0f)
    )

    Box(
        modifier = modifier
            .background(brush = brush)
    )
}


