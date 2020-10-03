package com.dev.moskal.postbrowser.app

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.dev.moskal.postbrowser.domain.model.POST_NOT_SELECTED_ID
import com.dev.moskal.postbrowser.domain.model.Resource
import com.dev.moskal.postbrowser.domain.usecase.GetPost
import com.dev.moskal.postbrowser.domain.usecase.InitialFetch
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
    initialFetch: InitialFetch,
    private val getPost: GetPost
) : ViewModel() {

    private val _selectedPost = MutableLiveData(POST_NOT_SELECTED_ID)
    val selectedPost: LiveData<Int>
        get() = _selectedPost
    val isPostSelected = selectedPost.map { it != POST_NOT_SELECTED_ID }

    private var observePostDeleteJob: Job? = null

    fun selectPost(postId: Int) {
        if (_selectedPost.value != postId) {
            _selectedPost.value = postId
            Timber.d("__Select post id=$postId")
            unselectOnDelete(postId)
        }
    }

    fun unselectPost() {
        Timber.d("__Unselected post id=${_selectedPost.value}")
        _selectedPost.value = POST_NOT_SELECTED_ID
        observePostDeleteJob?.cancel()
    }

    private fun unselectOnDelete(postId: Int) {
        observePostDeleteJob?.cancel()
        observePostDeleteJob = viewModelScope.launch {
            getPost.execute(postId)
                .filter { it is Resource.Success && it.data == null }
                .take(1)
                .collect {
                    Timber.d("__Selected post id=$postId deleted")
                    unselectPost()
                }
        }
    }

    init {
        viewModelScope.launch {
            initialFetch.execute()
        }
    }
}