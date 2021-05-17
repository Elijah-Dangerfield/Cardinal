package com.dangerfield.cardinal.data.network.model

data class ArticlePublishersResponse(
    val sources: List<Source>,
    val status: String
)