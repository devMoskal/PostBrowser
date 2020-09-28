package com.dev.moskal.postbrowser.app.postlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dev.moskal.postbrowser.app.postlist.PostItemClickListener.Action.DELETE_CLICK
import com.dev.moskal.postbrowser.app.postlist.PostItemClickListener.Action.ITEM_CLICK
import com.dev.moskal.postbrowser.domain.model.PostInfo
import com.dev.moskal.postbrowser.domain.model.Resource
import com.dev.moskal.postbrowser.domain.usecase.DeletePost
import com.dev.moskal.postbrowser.domain.usecase.GetPostsInfo
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class PostListViewModel @ViewModelInject constructor(
    getPostsInfoInfo: GetPostsInfo,
    private val deletePost: DeletePost
) : ViewModel(), PostItemClickListener {

    val viewState = getPostsInfoInfo.execute()
        .map(::reducePostListToViewState)
        .onStart { emit(PostListViewState.LOADING) }
        .asLiveData()

    override fun onClick(item: PostListItem.PostItem, action: PostItemClickListener.Action) {
        when (action) {
            ITEM_CLICK -> Timber.i("### onClick $item")
            DELETE_CLICK -> handleDeleteClick(item)
        }
    }

    private fun handleDeleteClick(item: PostListItem.PostItem) {
        viewModelScope.launch {
            val result = deletePost.execute(item.id)
        }
    }

    private fun reducePostListToViewState(resource: Resource<List<PostInfo>>) =
        when (resource) {
            is Resource.Success -> PostListViewState(resource.toItemsList())
            is Resource.Error -> PostListViewState(isError = true)
        }

    private fun Resource.Success<List<PostInfo>>.toItemsList() =
        data?.map(PostListItem::PostItem) ?: emptyList()
}