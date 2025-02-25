package com.example.vktestappvideoplayer.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vktestappvideoplayer.data.local.model.CachedVideoModel

/**
 * Defines the Room database for storing video data.
 */
@Database(entities = [CachedVideoModel::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to VideoDao for database operations.
     */
    abstract fun videoDao(): VideoDao

    companion object {
        private const val DB_NAME = "FavouriteDatabase"
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()

        /**
         * Returns a singleton instance of the database, creating it if necessary.
         */
        fun getInstance(context: Context): AppDatabase {
            INSTANCE?.let { return it }
            synchronized(LOCK) {
                INSTANCE?.let { return it }
                val database = Room.databaseBuilder(
                    context = context,
                    klass = AppDatabase::class.java,
                    name = DB_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = database
                return database
            }
        }
    }
}