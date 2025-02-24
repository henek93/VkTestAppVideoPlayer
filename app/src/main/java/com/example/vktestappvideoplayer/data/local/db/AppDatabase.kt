package com.example.vktestappvideoplayer.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vktestappvideoplayer.data.local.model.CachedVideoModel

/**
 * Room Database для хранения данных о видео.
 *
 * Используется для работы с локальной базой данных, которая хранит информацию о видео.
 * База данных создается с использованием Room и поддерживает миграции.
 *
 * @see RoomDatabase
 *
 * @property videoDao DAO для работы с таблицей видео.
 *
 */
@Database(entities = [CachedVideoModel::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

    companion object {
        private const val DB_NAME = "FavouriteDatabase"
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()


        fun getInstance(context: Context): AppDatabase {
            INSTANCE?.let { return it }

            synchronized(LOCK) {
                INSTANCE?.let { return it }

                val database = Room.databaseBuilder(
                    context = context,
                    klass = AppDatabase::class.java,
                    name = DB_NAME
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = database
                return database
            }
        }
    }
}