package com.example.vktestappvideoplayer.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    videoListScreenContent: @Composable () -> Unit,
    videoPlayerScreenContent: @Composable (String) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.VideoList.route
    ) {
        composable(Screen.VideoList.route) {
            videoListScreenContent()
        }

        composable(
            route = Screen.VideoPlayer.route,
            arguments = listOf(
                navArgument(name = "videoUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val videoUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
            val decoderUrl = Uri.decode(videoUrl)
            videoPlayerScreenContent(decoderUrl)
        }
    }
}
