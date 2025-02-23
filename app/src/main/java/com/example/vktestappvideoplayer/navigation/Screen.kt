package com.example.vktestappvideoplayer.navigation

import android.net.Uri

sealed class Screen(val route: String) {

    object VideoList : Screen(VIDEO_LIST_ROUTE)

    object VideoPlayer : Screen(VIDEO_PLAYER_ROUTE) {
        fun getRouteWithArgs(videoUrl: String): String {
            return "video_player/${Uri.encode(videoUrl)}"
        }
    }

    private companion object {

        const val VIDEO_LIST_ROUTE = "video_list"

        const val VIDEO_PLAYER_ROUTE = "video_player/{videoUrl}"
    }
}