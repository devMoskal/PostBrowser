package com.dev.moskal.postbrowser.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_ALBUMS
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_PHOTOS
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_POSTS
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_USERS

// Note: I made an assumption that only used filed should be cached in database.
// As we don't delete User nor albums foreignKeys are kind of redundant here at this stage.

@Entity(
    tableName = TABLE_POSTS, foreignKeys = [ForeignKey(
        entity = DbUser::class,
        parentColumns = ["userId"],
        childColumns = ["authorUserId"],
        onDelete = CASCADE
    )]
)
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

@Entity(
    tableName = TABLE_ALBUMS, foreignKeys = [ForeignKey(
        entity = DbUser::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = CASCADE
    )]
)
data class DbAlbum(
    @PrimaryKey val albumId: Int,
    val userId: Int,
    val title: String
)

@Entity(
    tableName = TABLE_PHOTOS, foreignKeys = [ForeignKey(
        entity = DbAlbum::class,
        parentColumns = ["albumId"],
        childColumns = ["parentAlbumId"],
        onDelete = CASCADE
    )]
)
data class DbPhoto(
    @PrimaryKey val photoId: Int,
    val parentAlbumId: Int,
    val title: String,
    val url: String
)