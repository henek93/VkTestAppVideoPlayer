package com.example.vktestappvideoplayer.data.network.api

import com.example.vktestappvideoplayer.data.network.dto.PexelsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines API endpoints for Pexels video data.
 */
interface PexelsApiService {
    /**
     * Fetches popular videos from Pexels.
     */
    @GET("videos/popular")
    suspend fun getPopularVideos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 25,
        @Query("locale") locale: String = "ru-RU",
        @Query("orientation") orientation: String = "landscape"
    ): PexelsResponseDto
}