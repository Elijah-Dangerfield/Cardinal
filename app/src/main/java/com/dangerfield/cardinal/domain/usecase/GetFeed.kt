package com.dangerfield.cardinal.domain.usecase

import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.repository.ArticleRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetFeed (
    private val getUsersCategories: GetUsersCategories,
    private val articleRepository: ArticleRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    ){

    /*
    Feed is built from:
    - All of the top headlines for users subscribed categories
    - Just the general top headlines
    - sorted by publish date (eventually also adding weight to most interacted categories and publishers)
     */
    fun invoke() : Flow<List<Article>> =
        flow {
            val categories = getUsersCategories.invoke()

            val categoricalHeadlines = withContext(dispatcher) {
                categories.map { category ->
                    async {
                        articleRepository.fetchNewTopHeadlinesForCategory(category)
                    }
                }.awaitAll()
            }

            val generalHeadlines = articleRepository.fetchNewTopHeadlinesGeneral()
            val feed = arrayListOf<Article>()
            feed.addAll(categoricalHeadlines.flatten() + generalHeadlines)
            feed.sortBy { it.publishedAt }
            emit(feed)
        }
}