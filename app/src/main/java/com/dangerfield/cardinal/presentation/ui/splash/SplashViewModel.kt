package com.dangerfield.cardinal.presentation.ui.splash

import androidx.lifecycle.ViewModel
import com.dangerfield.cardinal.domain.usecase.HasUserSelectedCategories
import com.dangerfield.cardinal.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    hasUserSelectedCategories: HasUserSelectedCategories
) : ViewModel() {
    private val _userNeedsToSelectCategoires: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val userNeedsToSelectCategoires: SingleLiveEvent<Boolean>
        get() = _userNeedsToSelectCategoires

    init {
        _userNeedsToSelectCategoires.value = !hasUserSelectedCategories.invoke()
    }
}
