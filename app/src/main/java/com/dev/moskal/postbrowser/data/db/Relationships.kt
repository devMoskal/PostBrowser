package com.dev.moskal.postbrowser.data.db

import androidx.room.Embedded
import androidx.room.Relation

data class DbPostAndUser(
    @Embedded val user: DbUser,
    @Relation(
        parentColumn = "userId",
        entityColumn = "authorUserId"
    )
    val post: DbPost,
)