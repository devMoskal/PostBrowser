package com.dev.moskal.postbrowser.app.postlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dev.moskal.postbrowser.app.postlist.PostListItem.PostLoadingItem
import com.dev.moskal.postbrowser.domain.model.PostInfo
import com.dev.moskal.postbrowser.domain.model.Resource
import com.dev.moskal.postbrowser.domain.usecase.GetPostsInfo
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class PostListViewModel @ViewModelInject constructor(
    getPostsInfoInfo: GetPostsInfo
) : ViewModel() {

    val viewState = getPostsInfoInfo.execute()
        .map(::reducePostListToViewState)
        .onStart { emit(PostListViewState.LOADING) }
        .asLiveData()

    private fun reducePostListToViewState(resource: Resource<List<PostInfo>>) =
        when (resource) {
            is Resource.Success -> PostListViewState(resource.toItemsList())
            is Resource.Error -> PostListViewState(isError = true)
        }

    private fun Resource.Success<List<PostInfo>>.toItemsList() =
        data?.map(PostListItem::PostItem) ?: emptyList()
}

data class PostListViewState(
    val items: List<PostListItem> = emptyList(),
    val isError: Boolean = false,
) {

    companion object {
        private const val LOADING_PLACEHOLDER_COUNT = 25
        val LOADING = PostListViewState(items = List(LOADING_PLACEHOLDER_COUNT) { PostLoadingItem })
    }
}