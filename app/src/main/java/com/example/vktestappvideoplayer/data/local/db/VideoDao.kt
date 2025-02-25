package com.example.vktestappvideoplayer.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vktestappvideoplayer.data.local.model.CachedVideoModel
import kotlinx.coroutines.flow.Flow

/**
 * Defines data access methods for video entities.
 */
@Dao
interface VideoDao {
    /**
     * Inserts or replaces a list of videos in the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videos: List<CachedVideoModel>)

    /**
     * Retrieves all cached videos as a Flow.
     */
    @Query("SELECT * FROM videos")
    fun getAll(): Flow<List<CachedVideoModel>>
}