package com.dangerfield.cardinal.data.cache.service

import androidx.room.*
import com.dangerfield.cardinal.data.cache.model.FeedItemCacheEntity
import com.dangerfield.cardinal.data.cache.model.OpenedArticleCacheEntity
import com.dangerfield.cardinal.data.cache.model.SearchedTermCacheEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface MainDao {
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

    //--------------Searched Terms--------------------

    /**
     * gets all searched terms
     */
    @Query("SELECT * from SEARCHED_TERM")
    fun getSearchedTerms(): Flow<List<SearchedTermCacheEntity>>

    /**
     * inserts a new searched term
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchedTerm(term: SearchedTermCacheEntity)


    /**
     * removes a specific searched term
     */
    @Query("DELETE from SEARCHED_TERM where value == :value")
    suspend fun deleteSearchedTerm(value: String)

    /**
     * removes all searched terms
     */
    @Query("DELETE from SEARCHED_TERM")
    suspend fun clearSearchedTerms()

}