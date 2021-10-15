package com.dangerfield.cardinal.data.cache.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dangerfield.cardinal.data.cache.model.ArticleSizeCacheEntity
import com.dangerfield.cardinal.data.cache.model.FeedItemCacheEntity
import com.dangerfield.cardinal.data.cache.model.OpenedArticleCacheEntity
import com.dangerfield.cardinal.data.cache.model.SearchedTermCacheEntity

@Database(
    entities = [FeedItemCacheEntity::class, OpenedArticleCacheEntity::class, SearchedTermCacheEntity::class, ArticleSizeCacheEntity::class], version = 2, exportSchema = false
)
abstract class CardinalDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun searchedTermDao(): SearchedTermDao

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