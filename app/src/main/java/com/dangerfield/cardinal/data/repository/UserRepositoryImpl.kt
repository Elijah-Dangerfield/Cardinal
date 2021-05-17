package com.dangerfield.cardinal.data.repository

import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.model.Category
import com.dangerfield.cardinal.domain.model.SearchedTerm
import com.dangerfield.cardinal.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    override fun getUsersCategories(): List<Category> {
        TODO("Not yet implemented")
    }

    override fun cacheUsersCategories(list: List<Category>) {
        TODO("Not yet implemented")
    }

    override fun getHasUserSelectedCategories(): Boolean {
        TODO("Not yet implemented")
    }

    override fun markUserHasSelectedCategories() {
        TODO("Not yet implemented")
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

    override suspend fun getUsersOpenedArticles(): List<Article> {
        TODO("Not yet implemented")
    }

    override suspend fun clearUsersOpenedArticles() {
        TODO("Not yet implemented")
    }

    override suspend fun addUserOpenedArticle(article: Article) {
        TODO("Not yet implemented")
    }
}