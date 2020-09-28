package com.dev.moskal.postbrowser.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_POST
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_USERS

@Entity(tableName = TABLE_POST)
data class DbPost(
    @PrimaryKey val postId: Int,
    val authorUserId: Int,
    val title: String,
    val body: String,
)

@Entity(tableName = TABLE_USERS)
data class DbUser(
    @PrimaryKey val userId: Int,
    val email: String
)