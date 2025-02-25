package com.example.vktestappvideoplayer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.vktestappvideoplayer.domain.entity.Video
import com.google.gson.Gson

/**
 * Defines the navigation graph for the app.
 */
@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    videoListScreenContent: @Composable () -> Unit,
    videoPlayerScreenContent: @Composable (Video) -> Unit
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
            arguments = listOf(navArgument(name = Screen.KEY_VIDEO) { type = NavType.StringType })
        ) { backStackEntry ->
            val videoJson = backStackEntry.arguments?.getString(Screen.KEY_VIDEO)
            val video = videoJson?.let { parseVideo(it) } ?: return@composable // Безопасная обработка
            videoPlayerScreenContent(video)
        }
    }
}

/**
 * Parses a JSON string into a Video object.
 */
private fun parseVideo(videoJson: String): Video? {
    return try {
        val decodedJson = java.net.URLDecoder.decode(videoJson, "UTF-8")
        Gson().fromJson(decodedJson, Video::class.java)
    } catch (e: Exception) {
        null // Можно добавить логирование
    }
}