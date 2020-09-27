package com.dev.moskal.postbrowser.data.db

import com.dev.moskal.postbrowser.domain.model.Post

fun List<Post>.mapPostDomainModelToDbEntity(): List<DbPost> = map {
    DbPost(it.id, it.userId, it.title, it.body)
}

fun List<DbPost>.mapPostDbEntityToDomainModel(): List<Post> = map {
    Post(it.id, it.userId, it.title, it.body)
}