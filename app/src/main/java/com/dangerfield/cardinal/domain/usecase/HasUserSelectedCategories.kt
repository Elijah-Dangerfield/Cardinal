package com.dangerfield.cardinal.domain.usecase

import com.dangerfield.cardinal.domain.repository.UserRepository

class HasUserSelectedCategories (
    private val userRepository: UserRepository
        ){

    fun invoke() : Boolean {
        return userRepository.getHasUserSelectedCategories()
    }
}