package com.example.vktestappvideoplayer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vktestappvideoplayer.domain.entity.Video

/**
 * Manages navigation state and actions.
 */
class NavigationState(
    val navHostController: NavHostController
) {
    /**
     * Navigates to the specified route with single-top behavior.
     */
    fun navigateTo(route: String) {
        navHostController.navigate(route) {
            launchSingleTop = true
            restoreState = true
        }
    }

    /**
     * Navigates to the video player screen with the given video.
     */
    fun navigateToVideoPlayer(video: Video) {
        navHostController.navigate(Screen.VideoPlayer.getRouteWithArgs(video)) {
            restoreState = true
        }
    }
}

/**
 * Creates and remembers a NavigationState instance.
 */
@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember { NavigationState(navHostController) }
}