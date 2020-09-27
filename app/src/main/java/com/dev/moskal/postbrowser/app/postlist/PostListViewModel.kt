package com.dev.moskal.postbrowser.app.postlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dev.moskal.postbrowser.app.postlist.PostListItem.PostLoadingItem
import com.dev.moskal.postbrowser.domain.model.Post
import com.dev.moskal.postbrowser.domain.model.Resource
import com.dev.moskal.postbrowser.domain.usecase.GetPosts
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class PostListViewModel @ViewModelInject constructor(
    getPosts: GetPosts
) : ViewModel() {

    val viewState = getPosts.execute()
        .map(::reducePostListToViewState)
        .onStart { emit(PostListViewState.LOADING) }
        .asLiveData()

    private fun reducePostListToViewState(it: Resource<List<Post>>) =
        when (it) {
            is Resource.Success -> PostListViewState(items = it.data?.map { post ->
                PostListItem.PostItem(
                    post
                )
            } ?: emptyList())
            is Resource.Error -> PostListViewState(isError = true)
        }
}

data class PostListViewState(
    val isError: Boolean = false,
    val items: List<PostListItem> = emptyList()
) {

    companion object {
        private const val LOADING_PLACEHOLDER_COUNT = 25
        val LOADING = PostListViewState(items = List(LOADING_PLACEHOLDER_COUNT) { PostLoadingItem })
    }
}