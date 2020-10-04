package com.dev.moskal.postbrowser.app.postlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.dev.moskal.postbrowser.app.postlist.PostItemClickListener.Action.DELETE_CLICK
import com.dev.moskal.postbrowser.app.postlist.PostItemClickListener.Action.ITEM_CLICK
import com.dev.moskal.postbrowser.app.util.SingleLiveEvent
import com.dev.moskal.postbrowser.domain.model.PostInfo
import com.dev.moskal.postbrowser.domain.model.Resource
import com.dev.moskal.postbrowser.domain.usecase.DeletePost
import com.dev.moskal.postbrowser.domain.usecase.GetPostsInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PostListViewModel @ViewModelInject constructor(
    getPostsInfo: GetPostsInfo,
    private val deletePost: DeletePost
) : ViewModel(), PostItemClickListener {

    // As for now data-binding does not have flow support
    // so we need to use backing LiveData with asFlow operator.
    val query = MutableLiveData("")

    var items = query.asFlow()
        .flatMapLatest(getPostsInfo::execute)
        .mapToItems()
        .onStartEmitLoading()
        .cachedIn(viewModelScope)

    private val _actions = SingleLiveEvent<PostListViewAction>()
    val actions: SingleLiveEvent<PostListViewAction>
        get() = _actions

    override fun onClick(item: PostListItem.PostItem, action: PostItemClickListener.Action) {
        when (action) {
            ITEM_CLICK -> sendSelectAction(item)
            DELETE_CLICK -> handleDeleteClick(item)
        }
    }

    fun onPagingStateChanged(loadState: CombinedLoadStates) {
        if (loadState.source.refresh is LoadState.Error) {
            // add error support
        } else {
            val uncriticalLoadingError = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            uncriticalLoadingError?.let {
                _actions.value = PostListViewAction.FailedToLoadMorePosts
            }
        }
    }

    private fun sendSelectAction(item: PostListItem.PostItem) {
        _actions.value = PostListViewAction.OnPostSelected(item.id)
    }

    private fun handleDeleteClick(item: PostListItem.PostItem) {
        viewModelScope.launch {
            val result = deletePost.execute(item.id)
            if (result is Resource.Error) {
                _actions.value = PostListViewAction.FailedToDeletePost
            }
        }
    }

    private fun Flow<PagingData<PostInfo>>.mapToItems(): Flow<PagingData<PostListItem>> =
        this.map { it.map(PostListItem::PostItem) }

    private fun Flow<PagingData<PostListItem>>.onStartEmitLoading(): Flow<PagingData<PostListItem>> =
        onStart { emit(PagingData.from(PLACEHOLDER_LIST)) }

    companion object {
        private const val LOADING_PLACEHOLDER_COUNT = 25
        private val PLACEHOLDER_LIST =
            List(LOADING_PLACEHOLDER_COUNT) { PostListItem.PostLoadingItem }
    }
}