package com.dev.moskal.postbrowser.app.postlist

sealed class PostListViewAction {
    data class OnPostSelected(val postId: Int) : PostListViewAction()
    object FailedToDeletePost : PostListViewAction()
    object FailedToLoadMorePosts : PostListViewAction()
}