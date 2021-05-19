package com.dangerfield.cardinal.domain.usecase

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

    /*
    Feed is built from:
    - All of the top headlines for users subscribed categories
    - Just the general top headlines
    - sorted by publish date (eventually also adding weight to most interacted categories and publishers)
    - when pagination is added: start adding queries from the everything endpoint when we run out of top headlines
    - eventually also add blacklisting
     */
    fun invoke(): Flow<Resource<List<Article>, GenericError>> =
        flow {
            //get cached feed
            val cachedFeed = getCachedFeed()
            if (shouldFetchNewFeed(cachedFeed)) {
                val loading: Resource<List<Article>, GenericError> = Resource.Loading(cachedFeed)
                emit(loading)

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
                val cacheFlow : Flow<Resource<List<Article>, GenericError>> = getCachedFeedFlow().map {
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
            articleRepository.fetchCachedFeed().first()
        }.cacheData ?: listOf()
    }

    private suspend fun getCachedFeedFlow(): Flow<List<Article>> {
        return cacheCallWrapper.safeCacheCall(dispatcher) {
            articleRepository.fetchCachedFeed()
        }.cacheData ?: flowOf(listOf())
    }

    private suspend fun fetchNewFeed(): NetworkResponse<List<Article>> {
        val categories = getUsersCategories.invoke()
        val feedRequests = withContext(dispatcher) {

            categories.map { category ->
                async {
                    networkCallWrapper.safeNetworkCall(dispatcher, requestTimeout) {
                        articleRepository.fetchNewTopHeadlinesForCategory(category)
                    }
                }
            } + async {
                networkCallWrapper.safeNetworkCall(dispatcher, requestTimeout) {
                    articleRepository.fetchNewTopHeadlinesGeneral()
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
            }.flatten().sortedBy { it.publishedAt }
            NetworkResponse.Success(data = articles)
        }
    }

    //Gets the most common problem from the feed requests that failed
    private fun getFeedError(errors: List<NetworkError>): NetworkError {
        return errors.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
            ?: NetworkError.Unknown()
    }
}