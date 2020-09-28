package com.dev.moskal.postbrowser.data.db

import androidx.room.Embedded
import androidx.room.Relation

data class DbPostWithUser(
    @Embedded val post: DbPost,

    @Relation(
        parentColumn = "authorUserId",
        entityColumn = "userId"
    )
    val user: DbUser? = null,
)