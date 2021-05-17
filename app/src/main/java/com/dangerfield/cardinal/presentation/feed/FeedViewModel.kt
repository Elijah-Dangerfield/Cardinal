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

    private val _feed : MutableLiveData<List<Article>> = MutableLiveData()
    val feed : MutableLiveData<List<Article>>
        get() = _feed

    private val _feedLoading : MutableLiveData<Boolean> = MutableLiveData()
    val feedLoading : MutableLiveData<Boolean>
        get() = _feedLoading

    init {
        getFeed()
    }

    fun getFeed(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _feedLoading.postValue(true)
            getFeed.invoke().collect {
                _feedLoading.postValue(false)
                feed.value = it
            }
        }
    }
}