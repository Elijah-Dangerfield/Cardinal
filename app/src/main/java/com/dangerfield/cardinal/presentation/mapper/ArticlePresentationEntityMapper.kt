package com.dangerfield.cardinal.presentation.mapper

import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.usecase.GetArticleDisplaySize
import com.dangerfield.cardinal.presentation.model.ArticlePresentationEntity
import javax.inject.Inject

class ArticlePresentationEntityMapper @Inject constructor(
    private val getArticleDisplaySize: GetArticleDisplaySize
) {
    fun mapFromEntity(entity: ArticlePresentationEntity): Article {
        return Article(
            entity.id,
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

    suspend fun mapToEntity(domainModel: Article): ArticlePresentationEntity {
        return ArticlePresentationEntity(
            domainModel.id,
            domainModel.author,
            domainModel.content,
            domainModel.description,
            domainModel.publishedAt,
            domainModel.source,
            domainModel.title,
            domainModel.url,
            domainModel.urlToImage,
            getArticleDisplaySize.invoke(domainModel.id)
        )
    }
}


