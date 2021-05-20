package com.dangerfield.cardinal.presentation.model

data class CategoryPresentationEntity(
    val title: String,
    val url: String,
    var isSelected: Boolean
) {

    fun toggleSelected() {
        isSelected = !isSelected
    }
}