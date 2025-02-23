package com.example.vktestappvideoplayer.data.local.db

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

@UnstableApi
object VideoCache {
    private var cache: Cache? = null

    fun getCache(context: Context): Cache {
        if (cache == null) {
            val cacheDir = File(context.cacheDir, "exoplayer-cache")
            if (!cacheDir.exists()) cacheDir.mkdirs()
            cache = SimpleCache(
                cacheDir,
                NoOpCacheEvictor(),
                StandaloneDatabaseProvider(context)
            )
        }
        return cache!!
    }

    fun releaseCache() {
        cache?.release()
        cache = null
    }
}