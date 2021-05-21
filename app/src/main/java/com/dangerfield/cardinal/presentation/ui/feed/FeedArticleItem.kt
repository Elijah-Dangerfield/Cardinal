package com.dangerfield.cardinal.presentation.ui.feed

import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.presentation.model.ArticlePresentationEntity

interface FeedArticleItem {
    fun getArticle() : ArticlePresentationEntity
}