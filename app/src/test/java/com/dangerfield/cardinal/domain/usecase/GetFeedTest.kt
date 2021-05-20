package com.dangerfield.cardinal.domain.usecase

import com.dangerfield.cardinal.data.util.CacheCallWrapperImpl
import com.dangerfield.cardinal.data.util.NetworkCallWrapperImpl
import com.dangerfield.cardinal.domain.cache.CacheCallWrapper
import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.model.Source
import com.dangerfield.cardinal.domain.network.NetworkCallWrapper
import com.dangerfield.cardinal.domain.repository.ArticleRepository
import com.dangerfield.cardinal.domain.util.RateLimiter
import com.dangerfield.cardinal.domain.util.Resource
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetFeedTest {

    private val getUsersCategories: GetUsersCategories = mockk(relaxed = true)
    private val articleRepository: ArticleRepository = mockk(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()
    private val cacheCallWrapper: CacheCallWrapper = CacheCallWrapperImpl()
    private val networkCallWrapper: NetworkCallWrapper = NetworkCallWrapperImpl()
    private val rateLimiter: RateLimiter = mockk(relaxed = true)
    private val networkFeed = listOf(
        articleWithTitle("Title1"), articleWithTitle("Title2"), articleWithTitle("Title3")
    )
    private var cachedFeed = listOf(
        articleWithTitle("Boom"), articleWithTitle("Pow"), articleWithTitle("Wow")
    )

    private val testSubject = GetFeed(getUsersCategories, articleRepository, rateLimiter, networkCallWrapper, cacheCallWrapper, testDispatcher)

    @Before
    fun setup() {
        //everything succeeds by default: Happy path to successful network update of cache
        coEvery { articleRepository.fetchTopHeadlinesForCategory(any()) } returns networkFeed
        coEvery { articleRepository.fetchTopHeadlinesGeneral() } returns networkFeed
        coEvery { articleRepository.getCachedFeed() } returns flowOf(cachedFeed)
        coEvery { articleRepository.replaceCachedFeed(any()) } returns Unit
        coEvery {rateLimiter.shouldFetch(any())} returns true
    }

    @Test
    fun `GIVEN initial cache call fails, WHEN getting feed, THEN we should make a network call to get feed, cache it, then show from cache `() = runBlockingTest {
        coEvery { articleRepository.getCachedFeed() } throws Exception()
        coEvery {rateLimiter.shouldFetch(any())} returns false //to make sure the fetch is happening based off cache, not based off rate limiter
        testSubject.invoke().onCompletion {
            coVerifyOrder {
                articleRepository.getCachedFeed()
                articleRepository.fetchTopHeadlinesForCategory(any())
                articleRepository.getCachedFeed()
            }
        }
    }

    @Test
    fun `GIVEN initial cache returns empty, WHEN getting feed, THEN we should make a network call to get feed, cache it, then show from cache `() = runBlockingTest {
        coEvery { articleRepository.getCachedFeed() } returns flowOf(listOf())
        coEvery {rateLimiter.shouldFetch(any())} returns false //to make sure the fetch is happening based off cache, not based off rate limiter
        testSubject.invoke().onCompletion {
            coVerifyOrder {
                articleRepository.getCachedFeed()
                articleRepository.fetchTopHeadlinesForCategory(any())
                articleRepository.getCachedFeed()
            }
        }
    }

    @Test
    fun `GIVEN rateLimiter wants a refresh, WHEN getting feed, THEN we should make a network call to get feed, cache it, then show from cache `() = runBlockingTest {
        testSubject.invoke().onCompletion {
            coVerifyOrder {
                articleRepository.getCachedFeed()
                articleRepository.fetchTopHeadlinesForCategory(any())
                articleRepository.getCachedFeed()
            }
        }
    }

    @Test
    fun `GIVEN rateLimiter doesnt want a refresh, WHEN getting feed, THEN we should only show from cache`() = runBlockingTest {
        coEvery {rateLimiter.shouldFetch(any())} returns false
        testSubject.invoke().onCompletion {
            coVerifyOrder {
                articleRepository.getCachedFeed() // to see if refresh is needed
                articleRepository.getCachedFeed() // to return to presentation layer
            }
            //no network calls should be made
            coVerify (exactly = 0){ articleRepository.fetchTopHeadlinesForCategory(any()) }
            coVerify (exactly = 0){ articleRepository.fetchTopHeadlinesGeneral() }
        }
    }

    @Test
    fun `GIVEN happy path, WHEN getting feed, THEN we should give loading and then a success from cache`() = runBlockingTest {
        val result = testSubject.invoke().toList()
        assert(result.first() is Resource.Loading)
        result[1].let {
            assert(it is Resource.Success && it.data == cachedFeed)
        }
    }

    @Test
    fun `GIVEN all network calls fail, WHEN getting feed, THEN we should give a failure with cache results`() = runBlockingTest {
        coEvery { articleRepository.fetchTopHeadlinesForCategory(any()) } throws Exception()
        coEvery { articleRepository.fetchTopHeadlinesGeneral() } throws Exception()
        val result = testSubject.invoke().toList().last()
        assert(result is Resource.Error)
    }

    @Test
    fun `GIVEN only one network calls fail, WHEN getting feed, THEN we should give a success with cache results`() = runBlockingTest {
        coEvery { articleRepository.fetchTopHeadlinesForCategory(any()) } throws Exception()
        coEvery { articleRepository.fetchTopHeadlinesGeneral() } returns networkFeed
        val result = testSubject.invoke().toList().last()
        assert(result is Resource.Success)
    }

    private fun articleWithTitle(title: String) = Article(
        "Author",
        "Content",
        "THis is a description",
        "123123erfrf",
        Source(id = "123", name = "batman"),
        title,
        "www.thing.com",
        "www.image.com"
    )

}