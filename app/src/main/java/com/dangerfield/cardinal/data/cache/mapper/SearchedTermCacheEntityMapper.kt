package com.dangerfield.cardinal.data.cache.mapper

import com.dangerfield.cardinal.data.cache.model.SearchedTermCacheEntity
import com.dangerfield.cardinal.domain.model.SearchedTerm
import com.dangerfield.cardinal.domain.util.EntityMapper

class SearchedTermCacheEntityMapper : EntityMapper<SearchedTermCacheEntity, SearchedTerm> {
    override fun mapFromEntity(entity: SearchedTermCacheEntity): SearchedTerm {
        return SearchedTerm(value = entity.value)
    }

    override fun mapToEntity(domainModel: SearchedTerm): SearchedTermCacheEntity {
        return SearchedTermCacheEntity(value = domainModel.value)
    }
}