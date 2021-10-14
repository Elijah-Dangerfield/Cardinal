package com.dangerfield.cardinal.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.dangerfield.cardinal.data.cache.mapper.ArticleSizeCacheEntityMapper
import com.dangerfield.cardinal.data.cache.mapper.FeedItemCacheEntityMapper
import com.dangerfield.cardinal.data.cache.mapper.SearchedTermCacheEntityMapper
import com.dangerfield.cardinal.data.cache.service.CardinalDatabase
import com.dangerfield.cardinal.data.cache.service.ArticleDao
import com.dangerfield.cardinal.data.cache.service.CategoryCacheService
import com.dangerfield.cardinal.data.cache.service.SearchedTermDao
import com.dangerfield.cardinal.data.network.mapper.TopHeadlineNetworkEntityMapper
import com.dangerfield.cardinal.data.network.service.NewsApiService
import com.dangerfield.cardinal.data.repository.ArticleRepositoryImpl
import com.dangerfield.cardinal.data.repository.UserRepositoryImpl
import com.dangerfield.cardinal.data.util.CacheCallWrapperImpl
import com.dangerfield.cardinal.data.util.NetworkCallWrapperImpl
import com.dangerfield.cardinal.data.util.RateLimiterImpl
import com.dangerfield.cardinal.domain.repository.ArticleRepository
import com.dangerfield.cardinal.domain.repository.UserRepository
import com.dangerfield.cardinal.domain.usecase.*
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
            .fallbackToDestructiveMigration() //TODO REMOVE
            .build()
    }

    @Singleton
    @Provides
    fun providesArticleDao(db: CardinalDatabase): ArticleDao {
        return db.articleDao()
    }

    @Singleton
    @Provides
    fun providesSearchedTermDao(db: CardinalDatabase): SearchedTermDao {
        return db.searchedTermDao()
    }

    @Singleton
    @Provides
    fun providesCategoryCacheService(
        @ApplicationContext context: Context,
        gson: Gson,
        sharedPreferences: SharedPreferences
    ): CategoryCacheService {
        return CategoryCacheService(sharedPreferences, gson, context.resources)
    }

    @Provides
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(context.applicationInfo.name, Context.MODE_PRIVATE)

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
    fun providesGetUsersCategoriesUseCase(userRepository: UserRepository): GetUsersCategories {
        return GetUsersCategories(userRepository)
    }

    @Singleton
    @Provides
    fun providesHasUserSelectedCategoriesUseCase(userRepository: UserRepository): HasUserSelectedCategories {
        return HasUserSelectedCategories(userRepository)
    }

    @Singleton
    @Provides
    fun providesGetArticleDisplaySizeUseCase(
        cacheCallWrapper: CacheCallWrapperImpl,
        repository: ArticleRepository,
    ): GetArticleDisplaySize {
        return GetArticleDisplaySize(cacheCallWrapper, repository)
    }

    @Singleton
    @Provides
    fun providesUserHasSelectedCategoriesUseCase(userRepository: UserRepository): SetUserHasSelectedCategories {
        return SetUserHasSelectedCategories(userRepository)
    }

    @Singleton
    @Provides
    fun providesSetUserCategoriesUseCase(userRepository: UserRepository): SetUsersCategories {
        return SetUsersCategories(userRepository)
    }

    @Singleton
    @Provides
    fun providesArticleRepository(
        topHeadlinesNetworkEntityMapper: TopHeadlineNetworkEntityMapper,
        newsApiService: NewsApiService,
        articleDao: ArticleDao,
        feedItemCacheEntityMapper: FeedItemCacheEntityMapper,
        articleSizeCacheEntityMapper: ArticleSizeCacheEntityMapper
    ): ArticleRepository {
        return ArticleRepositoryImpl(
            newsApiService,
            articleDao,
            topHeadlinesNetworkEntityMapper,
            feedItemCacheEntityMapper,
            articleSizeCacheEntityMapper
        )
    }

    @Singleton
    @Provides
    fun providesUserRepository(
        cacheService: CategoryCacheService,
        searchedTermDao: SearchedTermDao,
        searchedTermCacheEntityMapper: SearchedTermCacheEntityMapper
    ): UserRepository {
        return UserRepositoryImpl(cacheService, searchedTermDao, searchedTermCacheEntityMapper)
    }
}