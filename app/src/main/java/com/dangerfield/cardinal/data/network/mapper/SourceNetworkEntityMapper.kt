package com.dangerfield.cardinal.data.network.mapper

import com.dangerfield.cardinal.data.network.model.SourceNetworkEntity
import com.dangerfield.cardinal.domain.model.Source
import com.dangerfield.cardinal.domain.util.EntityMapper
import javax.inject.Inject

class SourceNetworkEntityMapper @Inject constructor() : EntityMapper<SourceNetworkEntity, Source> {
    override fun mapFromEntity(entity: SourceNetworkEntity): Source {
        return Source(id = entity.id, name = entity.name)
    }

    override fun mapToEntity(domainModel: Source): SourceNetworkEntity {
        return SourceNetworkEntity(id = domainModel.id, name = domainModel.name)
    }
}