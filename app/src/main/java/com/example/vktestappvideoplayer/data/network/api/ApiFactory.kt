package com.example.vktestappvideoplayer.data.network.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Provides a Retrofit instance for API calls.
 */
object ApiFactory {
    private const val BASE_URL = "https://api.pexels.com/v1/"
    private const val AUTHORIZATION_HEADER = "Authorization"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val originalRequest = chain.request()
                        val newRequest = originalRequest.newBuilder()
                            .header(AUTHORIZATION_HEADER, "pkYBlKnRDB8RzGrb11b4YNarnU7LbI4YvfWpEt5LAAQBeQsj6B8yV6wW")
                            .method(originalRequest.method, originalRequest.body)
                            .build()
                        chain.proceed(newRequest)
                    }
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .build()
    }

    /**
     * Provides the Pexels API service instance.
     */
    val apiService: PexelsApiService by lazy {
        retrofit.create(PexelsApiService::class.java)
    }
}