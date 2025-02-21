package com.example.vktestappvideoplayer.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vktestappvideoplayer.data.local.model.CachedVideoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videos: List<CachedVideoModel>)

    @Query("SELECT * FROM videos")
    fun getAll(): Flow<List<CachedVideoModel>>
}