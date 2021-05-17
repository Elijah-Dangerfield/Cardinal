package com.dangerfield.cardinal.data.network.service


import com.dangerfield.cardinal.BuildConfig
import com.dangerfield.cardinal.data.network.model.ArticlePublishersResponse
import com.dangerfield.cardinal.data.network.model.TopHeadlinesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines")
    suspend fun getTopHeadlinesGeneral(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = BuildConfig.apiKey
    ): TopHeadlinesResponse

    @GET("top-headlines")
    suspend fun getTopHeadlinesCategory(
        @Query("category") category: String,
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = BuildConfig.apiKey
    ): TopHeadlinesResponse

    @GET("sources")
    suspend fun getArticlePublishersGeneral(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = BuildConfig.apiKey
    ): ArticlePublishersResponse

    @GET("sources")
    suspend fun getArticlePublishersForCategory(
        @Query("category") category: String,
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = BuildConfig.apiKey
    ): ArticlePublishersResponse
}