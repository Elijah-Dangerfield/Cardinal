package com.dangerfield.cardinal.data.cache.mapper

import com.dangerfield.cardinal.data.cache.model.OpenedArticleCacheEntity
import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.model.Source
import com.dangerfield.cardinal.domain.util.EntityMapper

class OpenedArticleCacheEntityMapper : EntityMapper<OpenedArticleCacheEntity, Article> {
    override fun mapFromEntity(entity: OpenedArticleCacheEntity): Article {
        return Article(
            entity.id,
            entity.author,
            entity.content,
            entity.description,
            entity.publishedAt,
            Source(id = entity.sourceId, name = entity.sourceName),
            entity.title,
            entity.url,
            entity.urlToImage)
    }

    override fun mapToEntity(domainModel: Article): OpenedArticleCacheEntity {
        return OpenedArticleCacheEntity(
            domainModel.id,
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