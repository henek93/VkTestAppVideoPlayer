package com.example.vktestappvideoplayer.data.network.api

import com.example.vktestappvideoplayer.data.network.model.PexelsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsApiService {
    @GET("videos/popular")
    suspend fun getPopularVideos(
        @Query("per_page") perPage: Int = 5
    ): PexelsResponseDto
}