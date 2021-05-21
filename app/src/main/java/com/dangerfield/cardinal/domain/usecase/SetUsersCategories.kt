package com.dangerfield.cardinal.domain.usecase

import com.dangerfield.cardinal.domain.model.Category
import com.dangerfield.cardinal.domain.repository.UserRepository


class SetUsersCategories(
    private val userRepository: UserRepository
) {

    fun invoke(list: List<Category>) {
        userRepository.cacheUsersCategories(list)
    }
}