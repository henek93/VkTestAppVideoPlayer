package com.example.vktestappvideoplayer.presentation.videoList

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCbrt
import coil.compose.AsyncImage
import com.example.vktestappvideoplayer.R
import com.example.vktestappvideoplayer.domain.entity.Video

@Composable
fun VideoListScreen(
    paddingValues: PaddingValues,
    viewModel: VideoListViewModel,
    onVideoClick: (String) -> Unit
) {
    val videos by viewModel.videos.collectAsState()
    val isRefreshing = viewModel.isRefreshing.collectAsState()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (videos.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            VideoList(
                paddingValues = paddingValues,
                videos = videos,
                onVideoClick = { videoUrl ->
                    onVideoClick(videoUrl)
                },
                isRefreshing = isRefreshing.value,
                onRefresh = {
                    viewModel.refreshVideos()
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoList(
    paddingValues: PaddingValues,
    videos: List<Video>,
    onVideoClick: (String) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
) {
    PullToRefreshBox(
        modifier = Modifier.padding(paddingValues),
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
    ) {
        LazyColumn {
            items(videos) { video ->
                VideoItem(video = video, onVideoClick = onVideoClick)
            }
        }
    }

}

@Composable
fun VideoItem(video: Video, onVideoClick: (String) -> Unit) {

    var isImageLoaded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onVideoClick(video.videoUrl) })
            .padding(vertical = 8.dp)
    ) {
        // Миниатюра видео
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = "Video Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Продолжительность видео
            Box(
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${video.duration / 60}:${video.duration % 60}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Заголовок видео
        Text(
            text = video.title,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = video.authName,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }

//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onVideoClick(video.videoUrl) }
//            .padding(8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Box(
//            modifier = Modifier
//                .size(155.dp, 110.dp)
//        ) {
//            if (!isImageLoaded) {
//                // Shimmer эффект
//                ShimmerEffect(Modifier.matchParentSize())
//            }
//
//            // Загрузка изображения
//            AsyncImage(
//                model = video.thumbnailUrl,
//                contentDescription = null,
//                modifier = Modifier.matchParentSize(),
//                contentScale = ContentScale.Crop,
//                onSuccess = { isImageLoaded = true } // Отключаем shimmer после загрузки
//            )
//        }
//
//
//        Spacer(modifier = Modifier.width(10.dp))
//        Column(
//            modifier = Modifier.weight(1f)
//        ) {
//            Text(
//                text = video.title,
//                style = MaterialTheme.typography.headlineMedium,
//                fontSize = 14.sp,
//                modifier = Modifier.padding(8.dp),
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis
//            )
//
//            Spacer(modifier = Modifier.height(2.dp))
//
//            Text(
//                text = "${video.duration / 60}:${video.duration % 60}",
//                style = MaterialTheme.typography.headlineMedium,
//                fontSize = 14.sp,
//                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//        }
//    }
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


