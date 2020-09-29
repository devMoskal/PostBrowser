package com.dev.moskal.postbrowser.data.network

import com.dev.moskal.postbrowser.data.db.DbAlbum
import com.dev.moskal.postbrowser.data.db.DbPhoto
import com.dev.moskal.postbrowser.data.db.DbPost
import com.dev.moskal.postbrowser.data.db.DbUser
import com.dev.moskal.postbrowser.data.network.response.AlbumApiResponse
import com.dev.moskal.postbrowser.data.network.response.PhotoApiResponse
import com.dev.moskal.postbrowser.data.network.response.PostApiResponse
import com.dev.moskal.postbrowser.data.network.response.UserApiResponse

fun List<PostApiResponse>.mapPostApiResponseToDbEntity(): List<DbPost> = map {
    DbPost(it.id, it.userId ?: -1, it.title.orEmpty(), it.body.orEmpty())
}

fun List<UserApiResponse>.mapUserApiResponseToDbEntity(): List<DbUser> = map {
    DbUser(it.id, it.email.orEmpty())
}

fun List<AlbumApiResponse>.mapAlbumApiResponseToDbEntity(): List<DbAlbum> = map {
    DbAlbum(it.id, it.userId, it.title.orEmpty())
}

fun List<PhotoApiResponse>.mapPhotoApiResponseToDbEntity(): List<DbPhoto> = map {
    DbPhoto(it.id, it.albumId, it.url.orEmpty())
}