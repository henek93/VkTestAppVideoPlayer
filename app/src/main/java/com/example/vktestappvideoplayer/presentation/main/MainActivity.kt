package com.example.vktestappvideoplayer.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
            val navigationState = rememberNavigationState()
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

            VkTestAppVideoPlayerTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        Log.d("RecomposeMainActivity", "Recomopse")
                        TopAppBar(
                            title = {
                                Log.d("RecomposeMainActivity", "TopAppBar")
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
                            scrollBehavior = scrollBehavior
                        )
                    }
                ) { innerPadding ->
                    AppNavGraph(
                        navHostController = navigationState.navHostController,
                        videoListScreenContent = {
                            Log.d("RecomposeMainActivity", "VideoList")
                            VideoListScreen(
                                paddingValues = innerPadding,
                                viewModelFactory = viewModelFactory,
                                onVideoClick = { videoUrl ->
                                    navigationState.navigateToVideoPlayer(videoUrl)
                                },
                            )
                        },
                        videoPlayerScreenContent = { videoUrl ->
                            Log.d("RecomposeMainActivity", "VideoPlayer")
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
