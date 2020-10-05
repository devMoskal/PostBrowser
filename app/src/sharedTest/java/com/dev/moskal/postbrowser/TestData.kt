package com.dev.moskal.postbrowser

import com.dev.moskal.postbrowser.data.db.DbAlbum
import com.dev.moskal.postbrowser.data.db.DbPhoto
import com.dev.moskal.postbrowser.data.db.DbPost
import com.dev.moskal.postbrowser.data.db.DbUser
import com.dev.moskal.postbrowser.data.network.response.AlbumApiResponse
import com.dev.moskal.postbrowser.data.network.response.PhotoApiResponse
import com.dev.moskal.postbrowser.data.network.response.PostApiResponse
import com.dev.moskal.postbrowser.data.network.response.UserApiResponse

val testPosts = arrayListOf<DbPost>(
    DbPost(0, 0, "test1", "body1"),
    DbPost(1, 0, "test2", "body2"),
    DbPost(2, 1, "test3", "body3")
)

val testUsers = arrayListOf(
    DbUser(0, "test@email.com"),
    DbUser(1, "test2@email.com")
)

val testDbPosts = (0..100).map {
    DbPost(it, it / 10, "title$it", "body$it")
}

val testDbUsers = (0..12).map {
    DbUser(it, "user$it@email.com")
}

val testDbAlbums = (0..100).map {
    DbAlbum(it, it / 10, "album_title$it")
}

val testDbPhotos = (0..1000).map {
    DbPhoto(it, it / 10, "photo_title$it", "url$it")
}

val testApiPosts = (0..100).map {
    PostApiResponse(it, it / 10, "title$it!", "body$it")
}

val testApiUsers = (0..12).map {
    UserApiResponse(it, "name", "username$it", "user$it@email.com", null, null, null, null)
}

val testApiAlbums = (0..100).map {
    AlbumApiResponse(it, it / 10, "album_title$it")
}

val testApiPhotos = (0..1000).map {
    PhotoApiResponse(it, it / 10, "photo_title$it", "url$it", null)
}