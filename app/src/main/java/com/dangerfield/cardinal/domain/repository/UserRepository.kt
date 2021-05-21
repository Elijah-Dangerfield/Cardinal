package com.dangerfield.cardinal.domain.repository

import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.model.Category
import com.dangerfield.cardinal.domain.model.SearchedTerm
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    //users categories
    fun getUsersCategories() : List<Category>
    fun cacheUsersCategories(list: List<Category>)
    fun getHasUserSelectedCategories() : Boolean
    fun markUserHasSelectedCategories()

    //searched terms
    suspend fun getUsersSearchedTerms() : Flow<List<SearchedTerm>>
    suspend fun clearUsersSearchedTerms()
    suspend fun addUserSearchedTerm(term: SearchedTerm)
}