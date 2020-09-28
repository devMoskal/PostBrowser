package com.dev.moskal.postbrowser.app.postlist


import com.dev.moskal.postbrowser.app.postlist.PostListViewTypes.TYPE_LOADING
import com.dev.moskal.postbrowser.app.postlist.PostListViewTypes.TYPE_POST
import com.dev.moskal.postbrowser.domain.model.PostInfo

/**
 * Different types of item which can occur on [PostInfo]s list.
 * As for a sample app there is only one item
 */
sealed class PostListItem(val type: Int) {

    data class PostItem(
        val id: Int,
        val title: String,
        val email: String
    ) : PostListItem(TYPE_POST) {
        constructor(postInfo: PostInfo) : this(
            postInfo.id,
            postInfo.title,
            postInfo.userEmail
        )
    }

    object PostLoadingItem : PostListItem(TYPE_LOADING)
}