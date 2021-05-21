package com.dangerfield.cardinal.presentation.mapper

import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.util.EntityMapper
import com.dangerfield.cardinal.presentation.model.ArticlePresentationEntity
import com.dangerfield.cardinal.presentation.model.DisplaySize
import javax.inject.Inject

class ArticlePresentationEntityMapper @Inject constructor(): EntityMapper<ArticlePresentationEntity, Article> {
    override fun mapFromEntity(entity: ArticlePresentationEntity): Article {
        return Article(
            entity.author,
            entity.content,
            entity.description,
            entity.publishedAt,
            entity.source,
            entity.title,
            entity.url,
            entity.urlToImage
        )
    }

    override fun mapToEntity(domainModel: Article): ArticlePresentationEntity {
        return ArticlePresentationEntity(
            domainModel.author,
            domainModel.content,
            domainModel.description,
            domainModel.publishedAt,
            domainModel.source,
            domainModel.title,
            domainModel.url,
            domainModel.urlToImage,
            DisplaySize.Large //Large by default, assign some % to be small
        )
    }
}


