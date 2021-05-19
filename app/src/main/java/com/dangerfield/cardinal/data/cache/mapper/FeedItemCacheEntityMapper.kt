package com.dangerfield.cardinal.data.cache.mapper

import com.dangerfield.cardinal.data.cache.model.FeedItemCacheEntity
import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.model.Source
import com.dangerfield.cardinal.domain.util.EntityMapper
import javax.inject.Inject

class FeedItemCacheEntityMapper @Inject constructor(): EntityMapper<FeedItemCacheEntity, Article> {
    override fun mapFromEntity(entity: FeedItemCacheEntity): Article {
        return Article(
            entity.author,
            entity.content,
            entity.description,
            entity.publishedAt,
            Source(id = entity.sourceId, name = entity.sourceName),
            entity.title,
            entity.url,
            entity.urlToImage)
    }

    override fun mapToEntity(domainModel: Article): FeedItemCacheEntity {
        return FeedItemCacheEntity(
            null,
            author = domainModel.author,
            content = domainModel.content,
            description = domainModel.description,
            publishedAt = domainModel.publishedAt,
            sourceId = domainModel.source?.id,
            sourceName = domainModel.source?.name,
            title = domainModel.title,
            url = domainModel.url,
            urlToImage = domainModel.urlToImage)
    }
}