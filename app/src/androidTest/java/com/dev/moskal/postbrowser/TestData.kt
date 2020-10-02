package com.dev.moskal.postbrowser

import com.dev.moskal.postbrowser.data.db.DbPost
import com.dev.moskal.postbrowser.data.db.DbUser

val testPosts = arrayListOf<DbPost>(
    DbPost(0, 0, "test1", "body1"),
    DbPost(1, 0, "test2", "body2"),
    DbPost(2, 1, "test3", "body3")
)

val testUsers = arrayListOf(
    DbUser(0, "test@email.com"),
    DbUser(1, "test2@email.com")
)


