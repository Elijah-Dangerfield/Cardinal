package com.dangerfield.cardinal.domain.repository

import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    //building feed
    suspend fun fetchNewTopHeadlinesGeneral() : List<Article>
    suspend fun fetchNewTopHeadlinesForCategory(category: Category) : List<Article>
    suspend fun fetchNewEverythingForCategory(category: Category) : List<Article>

    //getting feed
    suspend fun fetchCachedFeed() : Flow<List<Article>>

    //updating feed
    suspend fun replaceCachedFeed(articles: List<Article>)

    //search
    suspend fun searchForArticles(term: String) : List<Article>

    //blacklist
    suspend fun isArticleBlackListed(article: Article) : Boolean
    suspend fun blacklistArticle(article: Article) : Boolean
}