package com.dev.moskal.postbrowser.app.postdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.moskal.postbrowser.domain.model.POST_NOT_SELECTED_ID
import com.dev.moskal.postbrowser.domain.model.Post
import com.dev.moskal.postbrowser.domain.usecase.GetPost
import com.dev.moskal.postbrowser.domain.usecase.GetUserAlbums
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

    private var currentPostId: Int = POST_NOT_SELECTED_ID

    fun selectPost(postId: Int?) {
        if (postId == currentPostId) return
        currentPostId = postId ?: POST_NOT_SELECTED_ID
        if (postId == null || postId == POST_NOT_SELECTED_ID) {
            _viewState.value = DetailsViewState.NO_POST_SELECTED
        } else {
            loadPost(postId)
        }
    }


    override fun onClick(album: DetailsListItem.AlbumItem) {
        viewModelScope.launch {
            viewState.value?.items?.let { list ->
                _viewState.value = reducer.reduceAfterAlbumClick(list, album)
            }
        }
    }

    private fun loadPost(postId: Int) = viewModelScope.launch {
        val post = getPost.execute(postId).data
        if (post != null) {
            _viewState.value = reducer.reducePost(post)
            loadAlbums(post)
        } else {
            handleError()
        }
    }

    private suspend fun loadAlbums(post: Post) {
        val albums = getUserAlbums.execute(post.userId).data
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