package com.dangerfield.cardinal.data.network.mapper

import com.dangerfield.cardinal.data.network.model.TopHeadlinesResponse
import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.util.EntityMapper
import javax.inject.Inject

class TopHeadlineNetworkEntityMapper @Inject constructor(
    private val articleMapper : ArticleNetworkEntityMapper
) : EntityMapper<TopHeadlinesResponse, List<Article>> {
    override fun mapFromEntity(entity: TopHeadlinesResponse): List<Article> {
        return entity.articleNetworkEntities.map { articleMapper.mapFromEntity(it) }
    }

    //Will never be used
    override fun mapToEntity(domainModel: List<Article>): TopHeadlinesResponse {
        return TopHeadlinesResponse(
            articleNetworkEntities = domainModel.map { articleMapper.mapToEntity(it) },
            totalResults = domainModel.size,
            status = "ok"
        )
    }
}