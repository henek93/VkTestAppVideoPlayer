package com.example.vktestappvideoplayer.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vktestappvideoplayer.R
import com.example.vktestappvideoplayer.navigation.AppNavGraph
import com.example.vktestappvideoplayer.navigation.rememberNavigationState
import com.example.vktestappvideoplayer.presentation.videoList.VideoListScreen
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

            val showTopAppBar = rememberSaveable { mutableStateOf(false) }
            VkTestAppVideoPlayerTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        if (showTopAppBar.value) {
                            TopAppBar(
                                title = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.surface),
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
                                            text = stringResource(R.string.newtube),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 30.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                },
                                scrollBehavior = scrollBehavior,
                            )
                        }
                    }
                ) { innerPadding ->
                    AppNavGraph(
                        navHostController = navigationState.navHostController,
                        videoListScreenContent = {
                            showTopAppBar.value = true
                            VideoListScreen(
                                paddingValues = innerPadding,
                                viewModelFactory = viewModelFactory,
                                onVideoClick = { videoUrl ->
                                    navigationState.navigateToVideoPlayer(videoUrl)
                                },
                            )
                        },
                        videoPlayerScreenContent = { video ->
                            showTopAppBar.value = false
                            VideoPlayerScreen(
                                video = video,
                                viewModelFactory = viewModelFactory,
                                onVideoClick = {
                                    navigationState.navigateToVideoPlayer(it)
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}
