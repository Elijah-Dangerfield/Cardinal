package com.dangerfield.cardinal.domain.usecase

import com.dangerfield.cardinal.domain.cache.CacheCallWrapper
import com.dangerfield.cardinal.domain.model.ArticleSize
import com.dangerfield.cardinal.domain.repository.ArticleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow

/*
So I thought it best to not tie display size to the domain, only presentation.
However, I do want to randomly have articles render as either large or small but i want it to be a
consistent user experience so I want
to cache the choice made and make sure an article is only ever rendered one way

This use case accesses our table entries for an article
 */
class GetArticleDisplaySize(
    private val cacheCallWrapper: CacheCallWrapper,
    private val articleRepository: ArticleRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) {

    suspend fun invoke(id: String) : ArticleSize {
        return cacheCallWrapper.safeCacheCall(dispatcher) {
            articleRepository.getArticleSize(id)
        }.cacheData ?: getAndAssignRandomArticleSize(id)
    }

    private suspend fun getAndAssignRandomArticleSize(id: String): ArticleSize {
        val size = ArticleSize.values().toList().random()
        cacheCallWrapper.safeCacheCall(dispatcher) { articleRepository.setArticleSize(id, size) }
        return size
    }
}