package com.dangerfield.cardinal.data.repository

import android.util.Log
import com.dangerfield.cardinal.data.cache.mapper.ArticleSizeCacheEntityMapper
import com.dangerfield.cardinal.data.cache.mapper.FeedItemCacheEntityMapper
import com.dangerfield.cardinal.data.cache.service.ArticleDao
import com.dangerfield.cardinal.data.network.mapper.TopHeadlineNetworkEntityMapper
import com.dangerfield.cardinal.data.network.service.NewsApiService
import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.model.ArticleSize
import com.dangerfield.cardinal.domain.model.Category
import com.dangerfield.cardinal.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticleRepositoryImpl(
    private val articlesService: NewsApiService,
    private val articleDao: ArticleDao,
    private val topHeadlinesNetworkEntityMapper: TopHeadlineNetworkEntityMapper,
    private val feedItemCacheEntityMapper: FeedItemCacheEntityMapper,
    private val articleSizeCacheEntityMapper: ArticleSizeCacheEntityMapper

) : ArticleRepository {

    override suspend fun fetchTopHeadlinesGeneral() = topHeadlinesNetworkEntityMapper
        .mapFromEntity(articlesService.getTopHeadlinesGeneral())

    override suspend fun fetchTopHeadlinesForCategory(category: Category) =
        topHeadlinesNetworkEntityMapper
            .mapFromEntity(
                articlesService
                    .getTopHeadlinesCategory(category.title)
            )

    override suspend fun fetchEverythingForCategory(category: Category): List<Article> {
        TODO("Not yet implemented")
    }

    override suspend fun getCachedFeed() = articleDao.getCachedFeed().map { articles ->
        articles.map { feedItemCacheEntityMapper.mapFromEntity(it) }
    }

    override suspend fun replaceCachedFeed(articles: List<Article>) {
        articleDao.replaceFeed(articles.map { feedItemCacheEntityMapper.mapToEntity(it) })
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

    override suspend fun getUsersOpenedArticles(): List<Article> {
        TODO("Not yet implemented")
    }

    override suspend fun clearUsersOpenedArticles() {
        TODO("Not yet implemented")
    }

    override suspend fun addUserOpenedArticle(article: Article) {
        TODO("Not yet implemented")
    }

    override suspend fun getArticleSize(id: String) =
        articleSizeCacheEntityMapper.mapFromEntity(articleDao.getArticleSize(id))


    override suspend fun setArticleSize(id: String, articleSize: ArticleSize) =
        articleDao.insertArticleSize(articleSizeCacheEntityMapper.mapToEntity(id, articleSize))
}