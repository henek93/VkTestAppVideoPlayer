package com.example.vktestappvideoplayer.navigation

import com.example.vktestappvideoplayer.domain.entity.Video
import com.google.gson.Gson
import okio.ByteString.Companion.encode

sealed class Screen(val route: String) {

    object VideoList : Screen(VIDEO_LIST_ROUTE)

    object VideoPlayer : Screen(VIDEO_PLAYER_ROUTE) {
        fun getRouteWithArgs(video: Video): String {
            val videoJson = Gson().toJson(video)
            val encodedVideoJson = java.net.URLEncoder.encode(videoJson, "UTF-8")
            return "video_player/$encodedVideoJson"
        }
    }

    companion object {

        const val KEY_VIDEO = "video"

        const val VIDEO_LIST_ROUTE = "video_list"

        const val VIDEO_PLAYER_ROUTE = "video_player/{video}"
    }
}