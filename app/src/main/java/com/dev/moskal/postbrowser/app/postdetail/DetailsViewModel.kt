package com.dev.moskal.postbrowser.app.postdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.moskal.postbrowser.domain.model.Album
import com.dev.moskal.postbrowser.domain.model.POST_NOT_SELECTED_ID
import com.dev.moskal.postbrowser.domain.model.Post
import com.dev.moskal.postbrowser.domain.model.Resource
import com.dev.moskal.postbrowser.domain.usecase.GetPost
import com.dev.moskal.postbrowser.domain.usecase.GetUserAlbums
import kotlinx.coroutines.launch

internal typealias AlbumItemId = Int

class DetailsViewModel @ViewModelInject constructor(
    private val getPost: GetPost,
    private val getUserAlbums: GetUserAlbums,
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


    private val cachedPhotoItems = HashMap<AlbumItemId, List<DetailsListItem.PhotoItem>>()

    override fun onClick(album: DetailsListItem.AlbumItem) {
        viewState.value?.items?.let { list ->
            _viewState.value = DetailsViewState(list.toMutableList().also { itemList ->
                val albumIndex = itemList.indexOf(album)
                if (albumIndex == -1) return
                itemList[albumIndex] = album.copy(isExpanded = !album.isExpanded)
                if (!album.isExpanded) {
                    cachedPhotoItems[album.id]?.let { itemList.addAll(albumIndex + 1, it) }
                } else {
                    val iterator = itemList.listIterator(albumIndex + 1)
                    while (iterator.hasNext()) {
                        val item = iterator.next()
                        if (item is DetailsListItem.AlbumItem) break
                        if (item is DetailsListItem.PhotoItem) {
                            iterator.remove()
                        }
                    }
                }


            })
        }
    }

    private fun loadPost(postId: Int) = viewModelScope.launch {
        getPost.execute(postId).apply {
            if (this is Resource.Success && data != null) {
                reducePostToViewState(data)
                getUserAlbums.execute(data.userId).apply {
                    if (this is Resource.Success && data != null) {
                        reduceAlbumsToViewState(data)
                    }
                    // TODO add album error handling
                }
            } else {
                handleError()
            }
        }
    }

    private fun reducePostToViewState(post: Post) {
        _viewState.value = DetailsViewState(listOf(DetailsListItem.HeaderItem(post)))
    }

    private fun reduceAlbumsToViewState(data: List<Album>) {
        val header = _viewState.value?.items?.first()
        val items = convertToItems(header, data)
        _viewState.value = DetailsViewState(items)
    }

    private fun convertToItems(
        header: DetailsListItem?,
        albums: List<Album>
    ): List<DetailsListItem> {
        val result = mutableListOf<DetailsListItem>()
        header?.let(result::add)
        albums.forEach { album ->
            val albumItem = DetailsListItem.AlbumItem(album)
            result.add(albumItem)
            val elements = album.photos.take(5)
                .map { DetailsListItem.PhotoItem(it, isVisible = albumItem.isExpanded) }
            cachedPhotoItems[albumItem.id] = elements
            //result.addAll(elements)
        }
        return result
    }


    private fun handleError() {
        // error handling should be a bit more sophisticated to handle each error differently
        // done for simplicity in sample app
        _viewState.value = DetailsViewState.ERROR
    }

    companion object {
        const val IS_ALBUM_COLLAPSED_BY_DEFAULT = true
    }
}