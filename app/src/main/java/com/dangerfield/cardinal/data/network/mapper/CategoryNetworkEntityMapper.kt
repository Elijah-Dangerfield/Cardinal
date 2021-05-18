package com.dangerfield.cardinal.data.network.mapper

import com.dangerfield.cardinal.domain.model.Category
import com.dangerfield.cardinal.domain.util.EntityMapper
import javax.inject.Inject

class CategoryNetworkEntityMapper @Inject constructor(): EntityMapper<Category, String> {
    override fun mapFromEntity(entity: Category): String {
        return when(entity) {
            Category.Technology -> "Technology"
            Category.Science -> "Science"
            Category.Business -> "Business"
            Category.Entertainment -> "Entertainment"
            Category.General -> "General"
            Category.Health -> "Health"
            Category.Sports -> "Sports"
        }
    }

    override fun mapToEntity(domainModel: String): Category {
        return when(domainModel) {
            "Technology" -> Category.Technology
            "Science" -> Category.Science
            "Business" -> Category.Business
            "Entertainment" ->Category.Entertainment
            "General" -> Category.General
            "Health" -> Category.Health
            else -> Category.Sports
        }
    }
}