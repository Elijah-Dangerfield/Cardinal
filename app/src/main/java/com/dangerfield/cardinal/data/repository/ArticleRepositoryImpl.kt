package com.dangerfield.cardinal.data.repository

import com.dangerfield.cardinal.data.cache.mapper.FeedItemCacheEntityMapper
import com.dangerfield.cardinal.data.cache.service.MainDao
import com.dangerfield.cardinal.data.network.mapper.CategoryNetworkEntityMapper
import com.dangerfield.cardinal.data.network.mapper.TopHeadlineNetworkEntityMapper
import com.dangerfield.cardinal.data.network.service.NewsApiService
import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.model.Category
import com.dangerfield.cardinal.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticleRepositoryImpl(
    private val articlesService: NewsApiService,
    private val mainDao: MainDao,
    private val topHeadlinesNetworkEntityMapper: TopHeadlineNetworkEntityMapper,
    private val categoryNetworkEntityMapper: CategoryNetworkEntityMapper,
    private val feedItemCacheEntityMapper: FeedItemCacheEntityMapper
) : ArticleRepository {

    override suspend fun fetchNewTopHeadlinesGeneral(): List<Article> {
        val result = articlesService.getTopHeadlinesGeneral()
        return topHeadlinesNetworkEntityMapper.mapFromEntity(result)
    }

    override suspend fun fetchNewTopHeadlinesForCategory(category: Category): List<Article> {
        return topHeadlinesNetworkEntityMapper
            .mapFromEntity(
                articlesService
                    .getTopHeadlinesCategory(categoryNetworkEntityMapper.mapFromEntity(category))
            )
    }

    override suspend fun fetchNewEverythingForCategory(category: Category): List<Article> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchCachedFeed(): Flow<List<Article>> {
        return mainDao.getCachedFeed().map { articles ->
            articles.map { feedItemCacheEntityMapper.mapFromEntity(it)}
        }
    }

    override suspend fun replaceCachedFeed(articles: List<Article>) {
        mainDao.replaceFeed(articles.map { feedItemCacheEntityMapper.mapToEntity(it) })
    }

    override suspend fun searchForArticles(term: String): List<Article> {
        TODO("Not yet implemented")
    }

    override suspend fun isArticleBlackListed(article: Article): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun blacklistArticle(article: Article): Boolean {
        TODO("Not yet implemented")
    }
}