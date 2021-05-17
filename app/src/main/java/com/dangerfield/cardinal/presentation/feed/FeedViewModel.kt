package com.dangerfield.cardinal.presentation.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.usecase.GetFeed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeed: GetFeed
) : ViewModel() {

    val feed  = MutableLiveData<List<Article>>()

    init {
        getFeed()
    }
    private fun getFeed() {
        viewModelScope.launch {
            getFeed.invoke().collect {
                feed.value = it
            }
        }
    }
}