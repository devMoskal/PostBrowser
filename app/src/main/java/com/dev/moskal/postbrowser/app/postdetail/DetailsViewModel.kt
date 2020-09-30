package com.dev.moskal.postbrowser.app.postdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.moskal.postbrowser.domain.model.POST_NOT_SELECTED_ID
import com.dev.moskal.postbrowser.domain.model.Post
import com.dev.moskal.postbrowser.domain.model.Resource
import com.dev.moskal.postbrowser.domain.usecase.GetPost
import kotlinx.coroutines.launch

class DetailsViewModel @ViewModelInject constructor(
    private val getPost: GetPost
) : ViewModel() {

    private val _viewState = MutableLiveData<DetailsViewState>(DetailsViewState.LOADING)
    val viewState: LiveData<DetailsViewState>
        get() = _viewState

    fun selectPost(postId: Int?) {
        if (postId == null || postId == POST_NOT_SELECTED_ID) {
            _viewState.value = DetailsViewState.NO_POST_SELECTED
        } else {
            loadPost(postId)
        }
    }

    private fun loadPost(postId: Int) = viewModelScope.launch {
        getPost.execute(postId).apply {
            if (this is Resource.Success && data != null) {
                reducePostToViewState(data)
            } else {
                handleError()
            }
        }
    }

    private fun reducePostToViewState(post: Post) {
        _viewState.value = DetailsViewState(post.title, post.body)
    }


    private fun handleError() {
        // error handling should be a bit more sophisticated to handle each error differently
        // done for simplicity in sample app
        _viewState.value = DetailsViewState.ERROR
    }
}

