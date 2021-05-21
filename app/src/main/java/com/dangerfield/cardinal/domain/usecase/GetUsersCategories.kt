package com.dangerfield.cardinal.domain.usecase

import com.dangerfield.cardinal.domain.model.Category
import com.dangerfield.cardinal.domain.repository.UserRepository

class GetUsersCategories(
    private val userRepository: UserRepository
) {

    fun invoke(): List<Category> {
        return userRepository.getUsersCategories()
    }
}