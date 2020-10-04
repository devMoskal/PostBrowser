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
import com.dev.moskal.postbrowser.domain.usecase.GetUserAlbums
import com.dev.moskal.postbrowser.domain.utils.filterWithSideEffect
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal typealias AlbumItemId = Int

class DetailsViewModel @ViewModelInject constructor(
    private val getPost: GetPost,
    private val getUserAlbums: GetUserAlbums,
    private val reducer: AlbumListReducer
) : ViewModel(), AlbumClickListener {

    private val _viewState = MutableLiveData(DetailsViewState.LOADING)
    val viewState: LiveData<DetailsViewState>
        get() = _viewState

    private var currentPostId: Int? = null
    private var observePostJob: Job? = null

    fun selectPost(postId: Int?) {
        if (postId == currentPostId) return
        currentPostId = postId ?: POST_NOT_SELECTED_ID
        if (postId == null || postId == POST_NOT_SELECTED_ID) {
            _viewState.value = DetailsViewState.NO_POST_SELECTED
        } else {
            _viewState.value = DetailsViewState.LOADING
            observePostJob?.cancel()
            observePostJob = observePost(postId)
        }
    }


    override fun onClick(album: DetailsListItem.AlbumItem) {
        viewModelScope.launch {
            viewState.value?.items?.let { list ->
                _viewState.value = reducer.reduceAfterAlbumClick(list, album)
            }
        }
    }

    private fun observePost(postId: Int) = viewModelScope.launch {
        getPost.execute(postId)
            .filterWithSideEffect({ it is Resource.Success }) {
                handleError()
            }
            .map { it.data }
            .collect(::handlePostReceived)
    }

    private suspend fun handlePostReceived(post: Post?) {
        _viewState.value = reducer.reducePost(post)
        post?.let {
            loadAlbums(it.userId)
        }
    }

    private suspend fun loadAlbums(userId: Int) {
        val albums = getUserAlbums.execute(userId).data
        if (albums != null) {
            val header = _viewState.value?.items?.first()
            _viewState.value = reducer.reduceAlbums(header, albums)
        } else {
            // TODO better handle album fetching error, no need to fail whole screen
            handleError()
        }
    }

    private fun handleError() {
        // error handling should be a bit more sophisticated to handle each error differently
        // done for simplicity in sample app
        _viewState.value = DetailsViewState.ERROR
    }
}