package com.dangerfield.cardinal.presentation.mapper

import com.dangerfield.cardinal.domain.model.Category
import com.dangerfield.cardinal.domain.usecase.GetUsersCategories
import com.dangerfield.cardinal.domain.util.EntityMapper
import com.dangerfield.cardinal.presentation.model.CategoryPresentationEntity
import javax.inject.Inject

class CategoryPresentationEntityMapper @Inject constructor(
    private val getUsersCategories: GetUsersCategories
): EntityMapper<CategoryPresentationEntity, Category> {

    override fun mapFromEntity(entity: CategoryPresentationEntity): Category {
        return Category.values().find {it.title == entity.title} ?: Category.Science
    }

    override fun mapToEntity(domainModel: Category): CategoryPresentationEntity {
        return CategoryPresentationEntity(
            title = domainModel.title,
            url = domainModel.url,
            isSelected = getUsersCategories.invoke().contains(domainModel)
        )
    }
}