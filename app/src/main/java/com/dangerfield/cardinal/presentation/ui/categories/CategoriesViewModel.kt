package com.dangerfield.cardinal.presentation.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dangerfield.cardinal.domain.model.Category
import com.dangerfield.cardinal.domain.usecase.SetUsersCategories
import com.dangerfield.cardinal.presentation.mapper.CategoryPresentationEntityMapper
import com.dangerfield.cardinal.presentation.model.CategoryPresentationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryPresentationEntityMapper: CategoryPresentationEntityMapper,
    private val setUsersCategories: SetUsersCategories
) : ViewModel() {

    enum class ButtonState { Next, Skip }

    val categories = Category.values().map { categoryPresentationEntityMapper.mapToEntity(it) }
    private val _usersCategories = MutableLiveData(getUsersInitialCategories())

    val buttonState: LiveData<ButtonState> = Transformations.map(_usersCategories) {
        if(it.isEmpty()) ButtonState.Skip else ButtonState.Next
    }

    fun addSelectedCategory(entity: CategoryPresentationEntity) {
        val category = categoryPresentationEntityMapper.mapFromEntity(entity)
        _usersCategories.value = _usersCategories.value?.plus(listOf(category))
    }

    fun removeSelectedCategory(entity: CategoryPresentationEntity) {
        val category = categoryPresentationEntityMapper.mapFromEntity(entity)
        _usersCategories.value = _usersCategories.value?.minus(listOf(category))
    }

    private fun getUsersInitialCategories(): List<Category> {
        return categories.filter { it.isSelected }
            .map { categoryPresentationEntityMapper.mapFromEntity(it) }
    }

    fun saveCurrentUserCategories() {

    }

}