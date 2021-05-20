package com.dangerfield.cardinal.presentation.categories

import androidx.lifecycle.ViewModel
import com.dangerfield.cardinal.domain.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(): ViewModel() {

    val categories = Category.values()
}