package com.dangerfield.cardinal.presentation.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.usecase.GetFeed
import com.dangerfield.cardinal.domain.util.GenericError
import com.dangerfield.cardinal.domain.util.Resource
import com.dangerfield.cardinal.presentation.util.SingleLiveEvent
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

    private val _feedError: SingleLiveEvent<GenericError> = SingleLiveEvent()
    val feedError: SingleLiveEvent<GenericError>
        get() = _feedError

    init {
        getFeed()
    }

    fun getFeed(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            getFeed.invoke().collect {
                _feedLoading.postValue(it is Resource.Loading)
                when(it) {
                    is Resource.Error -> {
                        _feedError.value = it.error
                        _feed.value = it.data ?: listOf()
                    }
                    is Resource.Loading -> {
                        _feed.value = it.data ?: listOf()
                    }
                    is Resource.Success -> {
                        _feed.value = it.data
                    }
                }
            }
        }
    }
}