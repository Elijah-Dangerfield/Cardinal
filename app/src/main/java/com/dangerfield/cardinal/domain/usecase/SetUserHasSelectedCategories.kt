package com.dangerfield.cardinal.domain.usecase

import com.dangerfield.cardinal.domain.repository.UserRepository

class SetUserHasSelectedCategories(
    private val userRepository: UserRepository
) {

    fun invoke() {
        userRepository.markUserHasSelectedCategories()
    }
}