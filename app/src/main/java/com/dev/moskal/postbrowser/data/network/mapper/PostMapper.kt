package com.dev.moskal.postbrowser.data.network.mapper

import com.dev.moskal.postbrowser.data.network.response.PostApiResponse
import com.dev.moskal.postbrowser.domain.model.Post

fun List<PostApiResponse>.mapPostAdiResponseToDomainModel(): List<Post> = map {
    Post(it.id, it.userId ?: -1, it.title.orEmpty(), it.body.orEmpty())
}
