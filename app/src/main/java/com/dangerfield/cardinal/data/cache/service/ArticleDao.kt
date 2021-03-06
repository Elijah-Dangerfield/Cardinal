package com.dangerfield.cardinal.data.cache.service

import androidx.room.*
import com.dangerfield.cardinal.data.cache.model.ArticleSizeCacheEntity
import com.dangerfield.cardinal.data.cache.model.FeedItemCacheEntity
import com.dangerfield.cardinal.data.cache.model.OpenedArticleCacheEntity
import com.dangerfield.cardinal.data.cache.model.SearchedTermCacheEntity
import com.dangerfield.cardinal.domain.model.ArticleSize
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticleDao {
    //--------------Feed--------------------
    /**
     * gets cached feed
     */
    @Query("SELECT * from FEED_ARTICLE")
    fun getCachedFeed(): Flow<List<FeedItemCacheEntity>>

    /**
     * inserts a feed
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeed(feed: List<FeedItemCacheEntity>)


    /**
     * Deletes all previous articles
     * inserts all new articles
     */
    @Transaction
    suspend fun replaceFeed(articles: List<FeedItemCacheEntity>) {
        deleteFeed()
        insertFeed(articles)
    }

    /**
     * removes all feed articles
     */
    @Query("DELETE from FEED_ARTICLE")
    suspend fun deleteFeed()

    //--------------Opened Articles--------------------

    /**
     * gets all opened articles from search
     */
    @Query("SELECT * from OPENED_ARTICLE")
    fun getOpenedArticles(): Flow<List<OpenedArticleCacheEntity>>

    /**
     * inserts a new opened article
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpenedArticle(article: OpenedArticleCacheEntity)


    /**
     * removes a specific opened article
     */
    @Query("DELETE from OPENED_ARTICLE where id == :id")
    suspend fun deleteOpenedArticle(id: String)

    /**
     * removes all opened articles
     */
    @Query("DELETE from OPENED_ARTICLE")
    suspend fun clearOpenedArticles()


    //--------------Util--------------------
    @Query("SELECT * from ARTICLE_SIZE where id == :id LIMIT 1")
    suspend fun getArticleSize(id: String): ArticleSizeCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticleSize(entity: ArticleSizeCacheEntity)

}