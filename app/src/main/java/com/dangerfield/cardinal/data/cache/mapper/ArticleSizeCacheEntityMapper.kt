package com.dangerfield.cardinal.data.cache.mapper

import com.dangerfield.cardinal.data.cache.model.ArticleSizeCacheEntity
import com.dangerfield.cardinal.domain.model.ArticleSize
import javax.inject.Inject

class ArticleSizeCacheEntityMapper @Inject constructor()  {
    fun mapFromEntity(entity: ArticleSizeCacheEntity?): ArticleSize? {
        return entity?.size?.let { ArticleSize.valueOf(it) }
    }

    fun mapToEntity(id: String, domainModel: ArticleSize): ArticleSizeCacheEntity {
        return ArticleSizeCacheEntity(id, domainModel.name)
    }
}