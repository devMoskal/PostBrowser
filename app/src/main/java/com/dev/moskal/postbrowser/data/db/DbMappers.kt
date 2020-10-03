package com.dev.moskal.postbrowser.data.db

import com.dev.moskal.postbrowser.domain.model.Album
import com.dev.moskal.postbrowser.domain.model.Photo
import com.dev.moskal.postbrowser.domain.model.Post
import com.dev.moskal.postbrowser.domain.model.PostInfo

fun DbPostWithUser.mapPostAndUserDbEntityToDomainModel() =
    PostInfo(post.postId, post.title, user?.email.orEmpty())


fun DbPost?.mapPostDbEntityToDomainModel(): Post? =
    this?.run { Post(postId, title, body, authorUserId) }

fun List<DbAlbumWithPhotos>.mapAlbumWithPhotosDbEntityToDomainModel(): List<Album> =
    map { (album, photos) ->
        Album(
            album.albumId,
            album.title,
            photos?.map { it.mapPhotoDbEntityToDomainModel() } ?: emptyList()
        )
    }

fun DbPhoto.mapPhotoDbEntityToDomainModel(): Photo = Photo(photoId, title, url)