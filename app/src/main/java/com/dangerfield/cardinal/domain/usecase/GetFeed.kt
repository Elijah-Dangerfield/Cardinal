package com.dangerfield.cardinal.domain.usecase

import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetFeed (
    private val articleRepository: ArticleRepository
    ){

    fun invoke() : Flow<List<Article>> =
        flow {
            val result = articleRepository.fetchNewTopHeadlinesGeneral()
            emit(result)
        }
}