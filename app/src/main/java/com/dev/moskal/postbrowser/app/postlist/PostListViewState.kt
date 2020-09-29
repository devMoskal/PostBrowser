package com.dev.moskal.postbrowser.app.postlist

data class PostListViewState(
    val items: List<PostListItem> = emptyList(),
    val isError: Boolean = false,
) {

    companion object {
        private const val LOADING_PLACEHOLDER_COUNT = 25
        val LOADING =
            PostListViewState(items = List(LOADING_PLACEHOLDER_COUNT) { PostListItem.PostLoadingItem })
    }
}

sealed class PostListViewAction {
    data class NavigateToPostDetails(val postId: Int) : PostListViewAction()
    object FailedToDeleteAction : PostListViewAction()
}