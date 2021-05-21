package com.dangerfield.cardinal.data.cache.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dangerfield.cardinal.data.cache.model.SearchedTermCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchedTermDao {
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