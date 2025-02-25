package com.example.vktestappvideoplayer.data.local.db

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

/**
 * Manages a video cache with an LRU eviction policy for efficient playback.
 */
@UnstableApi
object VideoCache {
    private var cache: Cache? = null
    private const val MAX_CACHE_SIZE = 100 * 1024 * 1024L // 100 MB

    /**
     * Returns the video cache instance, creating it if necessary with a size limit.
     */
    fun getCache(context: Context): Cache {
        if (cache == null) {
            val cacheDir = File(context.cacheDir, "exoplayer-cache")
            if (!cacheDir.exists()) cacheDir.mkdirs()
            cache = SimpleCache(
                cacheDir,
                LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE),
                StandaloneDatabaseProvider(context)
            )
        }
        return cache!!
    }

    /**
     * Releases the cache resources and clears all cached files from disk.
     */
    fun releaseCache() {
        cache?.let {
            it.keys.forEach { key -> it.removeResource(key) } // Очистка всех файлов
            it.release()
            cache = null
        }
    }
}