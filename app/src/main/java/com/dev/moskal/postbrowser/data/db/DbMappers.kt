package com.dev.moskal.postbrowser.data.db

import com.dev.moskal.postbrowser.domain.model.Post
import com.dev.moskal.postbrowser.domain.model.PostInfo

fun List<DbPostWithUser>.mapPostAndUserDbEntityToDomainModel(): List<PostInfo> = map {
    PostInfo(it.post.postId, it.post.title, it.user?.email.orEmpty())
}

fun DbPost.mapPostDbEntityToDomainModel(): Post = Post(postId, title, body, authorUserId)