package com.dangerfield.cardinal.di

import android.content.Context
import androidx.room.Room
import com.dangerfield.cardinal.data.cache.mapper.FeedItemCacheEntityMapper
import com.dangerfield.cardinal.data.cache.service.CardinalDatabase
import com.dangerfield.cardinal.data.cache.service.MainDao
import com.dangerfield.cardinal.data.network.mapper.TopHeadlineNetworkEntityMapper
import com.dangerfield.cardinal.data.network.service.NewsApiService
import com.dangerfield.cardinal.data.repository.ArticleRepositoryImpl
import com.dangerfield.cardinal.data.util.CacheCallWrapperImpl
import com.dangerfield.cardinal.data.util.NetworkCallWrapperImpl
import com.dangerfield.cardinal.data.util.RateLimiterImpl
import com.dangerfield.cardinal.domain.repository.ArticleRepository
import com.dangerfield.cardinal.domain.usecase.GetFeed
import com.dangerfield.cardinal.domain.usecase.GetUsersCategories
import com.dangerfield.cardinal.domain.usecase.HasUserSelectedCategories
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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
    fun providesCardinalDatabase(@ApplicationContext context: Context): CardinalDatabase {
        return Room.databaseBuilder(context, CardinalDatabase::class.java, "cardinal.db")
            .build()
    }

    @Singleton
    @Provides
    fun providesMainDao(db: CardinalDatabase): MainDao {
        return db.mainDao()
    }

    @Singleton
    @Provides
    fun providesGetFeedUseCase(
        repository: ArticleRepository,
        getUsersCategories: GetUsersCategories,
        networkCallWrapper: NetworkCallWrapperImpl,
        cacheCallWrapper: CacheCallWrapperImpl
    ): GetFeed {
        return GetFeed(
            getUsersCategories,
            repository,
            RateLimiterImpl(5, TimeUnit.MINUTES),
            networkCallWrapper,
            cacheCallWrapper
        )
    }

    @Singleton
    @Provides
    fun providesGetUsersCategoriesUseCase(): GetUsersCategories {
        return GetUsersCategories()
    }

    @Singleton
    @Provides
    fun providesHasUserSelectedCategoriesUseCase(): HasUserSelectedCategories {
        return HasUserSelectedCategories()
    }

    @Singleton
    @Provides
    fun providesArticleRepository(
        topHeadlinesNetworkEntityMapper: TopHeadlineNetworkEntityMapper,
        newsApiService: NewsApiService,
        mainDao: MainDao,
        feedItemCacheEntityMapper: FeedItemCacheEntityMapper
    ): ArticleRepository {
        return ArticleRepositoryImpl(
            newsApiService,
            mainDao,
            topHeadlinesNetworkEntityMapper,
            feedItemCacheEntityMapper
        )
    }
}