package com.example.vktestappvideoplayer.data.network.api

import com.example.vktestappvideoplayer.data.network.dto.PexelsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsApiService {
    @GET("videos/popular")
    suspend fun getPopularVideos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 25,
        @Query("locale") locale: String = "ru-RU"
    ): PexelsResponseDto
}