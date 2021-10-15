package com.dangerfield.cardinal.domain.usecase

import android.util.Log
import com.dangerfield.cardinal.domain.cache.CacheCallWrapper
import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.network.NetworkCallWrapper
import com.dangerfield.cardinal.domain.network.NetworkError
import com.dangerfield.cardinal.domain.network.NetworkResponse
import com.dangerfield.cardinal.domain.repository.ArticleRepository
import com.dangerfield.cardinal.domain.util.GenericError
import com.dangerfield.cardinal.domain.util.RateLimiter
import com.dangerfield.cardinal.domain.util.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * The feed is built from: top headlines for every category the user is subscribed to as well as
 * the general top headlines. Sorted by publish date
 * TODO add pagination, when we run out, start using queries from the everything endpoint with categories
 * TODO add blacklist checking
 * TODO if user has less than 3 categories, make the page size larger
 * TODO ensure no duplicates
 */
class GetFeed(
    private val getUsersCategories: GetUsersCategories,
    private val articleRepository: ArticleRepository,
    private val rateLimiter: RateLimiter,
    private val networkCallWrapper: NetworkCallWrapper,
    private val cacheCallWrapper: CacheCallWrapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val requestKey = "FEED_REQUEST"
    private val requestTimeout = 3000L

    fun invoke(forceRefresh: Boolean): Flow<Resource<List<Article>, GenericError>> =
        flow {
            val cachedFeed = getCachedFeed()
            if (shouldFetchNewFeed(cachedFeed) || forceRefresh) {
                // force refreshes only happen from swipe to refresh, we dont need to dispatch loading
                if(!forceRefresh) {
                    val loading: Resource<List<Article>, GenericError> = Resource.Loading(cachedFeed)
                    emit(loading)
                }

                val result: Flow<Resource<List<Article>, GenericError>> =
                    when (val networkResult = fetchNewFeed()) {
                        is NetworkResponse.Error -> {
                            val genericError = when (networkResult.error) {
                                is NetworkError.Timeout -> GenericError.NETWORK_TIMEOUT_ERROR
                                is NetworkError.Unknown -> GenericError.UNKNOWN
                                else -> GenericError.NETWORK_FAILURE_ERROR
                            }
                            getCachedFeedFlow().map {
                                Resource.Error(data = it, error = genericError)
                            }
                        }
                        is NetworkResponse.Success -> {
                            cacheFeed(networkResult.data)
                            getCachedFeedFlow().map {
                                Resource.Success(data = it)
                            }
                        }
                    }
                emitAll(result)

            } else {
                val cacheFlow: Flow<Resource<List<Article>, GenericError>> =
                    getCachedFeedFlow().map {
                        Resource.Success(data = it)
                    }
                emitAll(cacheFlow)
            }
        }

    private fun shouldFetchNewFeed(feed: List<Article>): Boolean {
        return feed.isEmpty() || rateLimiter.shouldFetch(requestKey)
    }

    private suspend fun cacheFeed(data: List<Article>) {
        cacheCallWrapper.safeCacheCall(dispatcher) {
            articleRepository.replaceCachedFeed(data)
        }
    }

    private suspend fun getCachedFeed(): List<Article> {
        return cacheCallWrapper.safeCacheCall(dispatcher) {
            articleRepository.getCachedFeed().first()
        }.cacheData ?: listOf()
    }

    private suspend fun getCachedFeedFlow(): Flow<List<Article>> {
        return cacheCallWrapper.safeCacheCall(dispatcher) {
            articleRepository.getCachedFeed()
        }.cacheData ?: flowOf(listOf())
    }

    private suspend fun fetchNewFeed(): NetworkResponse<List<Article>> {
        val categories = getUsersCategories.invoke()
        val feedRequests = withContext(dispatcher) {

            categories.map { category ->
                async {
                    networkCallWrapper.safeNetworkCall(dispatcher, requestTimeout) {
                        articleRepository.fetchTopHeadlinesForCategory(category)
                    }
                }
            } + async {
                networkCallWrapper.safeNetworkCall(dispatcher, requestTimeout) {
                    articleRepository.fetchTopHeadlinesGeneral()
                }
            }
        }

        val responses = feedRequests.awaitAll()
        //only a failure if they all failed
        return if (responses.count { it is NetworkResponse.Error } == responses.size) {
            //return failure response
            val errors = responses.mapNotNull { it.networkError }
            NetworkResponse.Error(error = getFeedError(errors))
        } else {
            //return success response
            val articles = responses.mapNotNull {
                it.networkData
            }.flatten().sortedBy { it.publishedAt }.distinctBy { it.url + it.title }
            NetworkResponse.Success(data = articles)
        }
    }

    //Gets the most common problem from the feed requests that failed
    private fun getFeedError(errors: List<NetworkError>): NetworkError {
        return errors.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
            ?: NetworkError.Unknown()
    }
}