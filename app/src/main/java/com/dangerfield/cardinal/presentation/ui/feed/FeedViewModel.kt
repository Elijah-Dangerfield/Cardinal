package com.dangerfield.cardinal.presentation.ui.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.usecase.GetFeed
import com.dangerfield.cardinal.domain.util.GenericError
import com.dangerfield.cardinal.domain.util.Resource
import com.dangerfield.cardinal.presentation.mapper.ArticlePresentationEntityMapper
import com.dangerfield.cardinal.presentation.model.ArticlePresentationEntity
import com.dangerfield.cardinal.presentation.model.DisplaySize
import com.dangerfield.cardinal.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeed: GetFeed,
    private val articlePresentationEntityMapper: ArticlePresentationEntityMapper
) : ViewModel() {


    private val _feed : MutableLiveData<List<ArticlePresentationEntity>> = MutableLiveData()
    val feed : MutableLiveData<List<ArticlePresentationEntity>>
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
                        pushFeedUpdate(it.data ?: listOf())
                    }
                    is Resource.Loading -> {
                        pushFeedUpdate(it.data ?: listOf())
                    }
                    is Resource.Success -> {
                        pushFeedUpdate(it.data )
                    }
                }
            }
        }
    }

    private fun pushFeedUpdate(list : List<Article>) {
        val presentationList = list.map { article ->
            articlePresentationEntityMapper.mapToEntity(article).also {
                if ((0..10).random() < 3) {
                    //3 out of 10 will be small
                    it.displaySize = DisplaySize.Small
                }
            }
        }
        _feed.value = presentationList
    }
}