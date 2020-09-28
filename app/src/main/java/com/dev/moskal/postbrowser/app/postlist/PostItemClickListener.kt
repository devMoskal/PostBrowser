package com.dev.moskal.postbrowser.app.postlist

/**
 * Listener used to process user interaction with [PostListItem.PostItem]
 */
interface PostItemClickListener {
    fun onClick(item: PostListItem.PostItem, action: Action)

    enum class Action {
        ITEM_CLICK,
        DELETE_CLICK
    }
}