package com.dangerfield.cardinal.data.cache.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dangerfield.cardinal.data.cache.model.FeedItemCacheEntity
import com.dangerfield.cardinal.data.cache.model.OpenedArticleCacheEntity
import com.dangerfield.cardinal.data.cache.model.SearchedTermCacheEntity

@Database(
    entities = [FeedItemCacheEntity::class, OpenedArticleCacheEntity::class, SearchedTermCacheEntity::class], version = 1, exportSchema = false
)
abstract class CardinalDatabase : RoomDatabase() {

    abstract fun mainDao(): MainDao

    companion object {
        @Volatile
        private var instance: CardinalDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            CardinalDatabase::class.java, "cardinal.db"
        )
            .build()
    }
}