package com.dangerfield.cardinal.domain.usecase

import com.dangerfield.cardinal.domain.model.Category

class GetUsersCategories {


    /*
    So this will use prefences to get back a list of category non-async
     */
    fun invoke()  : List<Category> {
        return listOf(Category.Technology, Category.Business, Category.Science)
    }
}