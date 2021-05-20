package com.dangerfield.cardinal.domain.repository

import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    //building feed
    suspend fun fetchTopHeadlinesGeneral() : List<Article>
    suspend fun fetchTopHeadlinesForCategory(category: Category) : List<Article>
    suspend fun fetchEverythingForCategory(category: Category) : List<Article>

    //getting feed
    suspend fun getCachedFeed() : Flow<List<Article>>

    //updating feed
    suspend fun replaceCachedFeed(articles: List<Article>)

    //search
    suspend fun searchForArticles(term: String) : List<Article>

    //blacklist
    suspend fun isArticleBlackListed(article: Article) : Boolean
    suspend fun blacklistArticle(article: Article) : Boolean
}