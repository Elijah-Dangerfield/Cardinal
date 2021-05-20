package com.dangerfield.cardinal.presentation

import androidx.lifecycle.ViewModel
import com.dangerfield.cardinal.domain.usecase.HasUserSelectedCategories
import com.dangerfield.cardinal.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val hasUserSelectedCategories: HasUserSelectedCategories
) : ViewModel(){
    private val _showCategoriesPage: SingleLiveEvent<Unit> = SingleLiveEvent()
    val showCategoriesPage: SingleLiveEvent<Unit>
        get() = _showCategoriesPage

    init {
        if(!hasUserSelectedCategories.invoke()) {
            _showCategoriesPage.call()
        }
    }
}