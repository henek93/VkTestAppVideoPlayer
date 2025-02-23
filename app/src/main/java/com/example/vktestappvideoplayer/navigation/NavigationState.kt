package com.example.vktestappvideoplayer.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vktestappvideoplayer.domain.entity.Video

class NavigationState(
    val navHostController: NavHostController
) {

    fun navigateTo(route: String) {
        navHostController.navigate(route) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToVideoPlayer(video: Video) {
        navHostController.navigate(Screen.VideoPlayer.getRouteWithArgs(video)) {
            restoreState = true
        }
    }

}

@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember {
        NavigationState(navHostController)
    }
}