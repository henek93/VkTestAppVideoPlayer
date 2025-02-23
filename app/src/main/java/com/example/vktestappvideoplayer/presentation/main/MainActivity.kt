package com.example.vktestappvideoplayer.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vktestappvideoplayer.R
import com.example.vktestappvideoplayer.navigation.AppNavGraph
import com.example.vktestappvideoplayer.navigation.rememberNavigationState
import com.example.vktestappvideoplayer.presentation.videoList.VideoListScreen
import com.example.vktestappvideoplayer.presentation.videoList.VideoListViewModel
import com.example.vktestappvideoplayer.presentation.videoPlayer.VideoPlayerScreen
import com.example.vktestappvideoplayer.ui.theme.VkTestAppVideoPlayerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = MainViewModelFactory(context = this@MainActivity)

        setContent {
            VkTestAppVideoPlayerTheme {

                val navigationState = rememberNavigationState()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_logo),
                                        contentDescription = "Logo",
                                        tint = Color(0xFFFF0000)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "NewTube",
                                        color = Color(0xFF282828),
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            },
                        )
                    }
                ) { innerPadding ->
                    AppNavGraph(
                        navHostController = navigationState.navHostController,
                        videoListScreenContent = {
                            VideoListScreen(
                                paddingValues = innerPadding,
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
