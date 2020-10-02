package com.dev.moskal.postbrowser.app

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.dev.moskal.postbrowser.domain.model.POST_NOT_SELECTED_ID
import com.dev.moskal.postbrowser.domain.usecase.InitialFetch
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    initialFetch: InitialFetch
) : ViewModel() {

    private val _selectedPost = MutableLiveData(POST_NOT_SELECTED_ID)
    val selectedPost: LiveData<Int>
        get() = _selectedPost
    val isPostSelected = selectedPost.map { it != POST_NOT_SELECTED_ID }

    fun selectPost(postId: Int) {
        _selectedPost.value = postId
    }

    fun unselectPost() {
        _selectedPost.value = POST_NOT_SELECTED_ID
    }

    init {
        viewModelScope.launch {
            initialFetch.execute()
        }
    }
}