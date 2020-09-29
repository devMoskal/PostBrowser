package com.dev.moskal.postbrowser.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_ALBUMS
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_PHOTOS
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_POSTS
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_USERS

@Entity(tableName = TABLE_POSTS)
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

@Entity(tableName = TABLE_ALBUMS)
data class DbAlbum(
    @PrimaryKey val albumId: Int,
    val userId: Int,
    val title: String
)

@Entity(tableName = TABLE_PHOTOS)
data class DbPhoto(
    @PrimaryKey val photoId: Int,
    val albumId: Int,
    val url: String
)