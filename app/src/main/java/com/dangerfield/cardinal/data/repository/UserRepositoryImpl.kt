package com.dangerfield.cardinal.data.repository

import com.dangerfield.cardinal.data.cache.service.CategoryCacheService
import com.dangerfield.cardinal.domain.model.Category
import com.dangerfield.cardinal.domain.model.SearchedTerm
import com.dangerfield.cardinal.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val categoryCacheService: CategoryCacheService
        ): UserRepository {
    override fun getUsersCategories(): List<Category> {
        return categoryCacheService.getUsersCategories()
    }

    override fun cacheUsersCategories(list: List<Category>) {
        categoryCacheService.setUsersCategories(list)
    }

    override fun getHasUserSelectedCategories(): Boolean {
        return categoryCacheService.getHasUserSelectedCategories()
    }

    override fun markUserHasSelectedCategories() {
        categoryCacheService.setUserHasSelectedCategories()
    }

    override suspend fun getUsersSearchedTerms(): List<SearchedTerm> {
        TODO("Not yet implemented")
    }

    override suspend fun clearUsersSearchedTerms() {
        TODO("Not yet implemented")
    }

    override suspend fun addUserSearchedTerm(term: SearchedTerm) {
        TODO("Not yet implemented")
    }
}