package com.dangerfield.cardinal.data.repository

import com.dangerfield.cardinal.data.cache.mapper.SearchedTermCacheEntityMapper
import com.dangerfield.cardinal.data.cache.service.CategoryCacheService
import com.dangerfield.cardinal.data.cache.service.SearchedTermDao
import com.dangerfield.cardinal.domain.model.Category
import com.dangerfield.cardinal.domain.model.SearchedTerm
import com.dangerfield.cardinal.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val categoryCacheService: CategoryCacheService,
    private val userSearchedTermDao: SearchedTermDao,
    private val searchedTermCacheEntityMapper: SearchedTermCacheEntityMapper
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

    override suspend fun getUsersSearchedTerms(): Flow<List<SearchedTerm>> {
        return userSearchedTermDao.getSearchedTerms().map { list ->
            list.map { searchedTermCacheEntityMapper.mapFromEntity(it) }
        }
    }

    override suspend fun clearUsersSearchedTerms() {
        userSearchedTermDao.clearSearchedTerms()
    }

    override suspend fun addUserSearchedTerm(term: SearchedTerm) {
        userSearchedTermDao.insertSearchedTerm(searchedTermCacheEntityMapper.mapToEntity(term))
    }
}