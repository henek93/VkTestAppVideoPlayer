package com.example.vktestappvideoplayer.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.vktestappvideoplayer.navigation.AppNavGraph
import com.example.vktestappvideoplayer.navigation.rememberNavigationState
import com.example.vktestappvideoplayer.presentation.videoList.VideoListScreen
import com.example.vktestappvideoplayer.presentation.videoList.VideoListViewModel
import com.example.vktestappvideoplayer.presentation.videoPlayer.VideoPlayerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = MainViewModelFactory(context = this@MainActivity)

        setContent {
            MaterialTheme {
                val navigationState = rememberNavigationState()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),

                    ) { innerPadding ->
                    AppNavGraph(
                        navHostController = navigationState.navHostController,
                        videoListScreenContent = {
                            VideoListScreen(
                                viewModel = viewModelFactory.create(VideoListViewModel::class.java),
                                onVideoClick = { videoUrl ->
                                    navigationState.navigateToVideoPlayer(videoUrl)
                                }
                            )
                        },
                        videoPlayerScreenContent = { videoUrl ->
                            VideoPlayerScreen(
                                videoUrl = videoUrl,
                            )
                        }
                    )
                }
            }
        }
    }
}
