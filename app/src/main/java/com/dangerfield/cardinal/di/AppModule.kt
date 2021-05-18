package com.dangerfield.cardinal.di

import com.dangerfield.cardinal.data.network.mapper.CategoryNetworkEntityMapper
import com.dangerfield.cardinal.data.network.mapper.TopHeadlineNetworkEntityMapper
import com.dangerfield.cardinal.data.network.service.NewsApiService
import com.dangerfield.cardinal.data.repository.ArticleRepositoryImpl
import com.dangerfield.cardinal.domain.repository.ArticleRepository
import com.dangerfield.cardinal.domain.usecase.GetFeed
import com.dangerfield.cardinal.domain.usecase.GetUsersCategories
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //makes this an application wide dependency
object AppModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun providesNewsApiService(gson: Gson): NewsApiService {

        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BASIC
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()


        return Retrofit.Builder().baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build().create(NewsApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesGetFeedUseCase(repository: ArticleRepository, getUsersCategories: GetUsersCategories) : GetFeed {
        return GetFeed(getUsersCategories, repository)
    }

    @Singleton
    @Provides
    fun providesGetUsersCategoriesUseCase() : GetUsersCategories {
        return GetUsersCategories()
    }

    @Singleton
    @Provides
    fun providesArticleRepository(
        topHeadlinesNetworkEntityMapper: TopHeadlineNetworkEntityMapper,
        newsApiService: NewsApiService,
        categoryNetworkEntityMapper: CategoryNetworkEntityMapper
    ): ArticleRepository {
        return ArticleRepositoryImpl(
            newsApiService,
            topHeadlinesNetworkEntityMapper,
            categoryNetworkEntityMapper)
    }
}