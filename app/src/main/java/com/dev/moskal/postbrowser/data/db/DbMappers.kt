package com.dev.moskal.postbrowser.data.db

import com.dev.moskal.postbrowser.domain.model.Album
import com.dev.moskal.postbrowser.domain.model.Photo
import com.dev.moskal.postbrowser.domain.model.Post
import com.dev.moskal.postbrowser.domain.model.PostInfo

fun List<DbPostWithUser>.mapPostAndUserDbEntityToDomainModel(): List<PostInfo> = map {
    PostInfo(it.post.postId, it.post.title, it.user?.email.orEmpty())
}

fun DbPost.mapPostDbEntityToDomainModel(): Post = Post(postId, title, body, authorUserId)

fun List<DbAlbumWithPhotos>.mapAlbumWithPhotosDbEntityToDomainModel(): List<Album> =
    map { (album, photos) ->
        Album(
            album.albumId,
            album.title,
            photos?.map { it.mapPhotoDbEntityToDomainModel() } ?: emptyList()
        )
    }

fun DbPhoto.mapPhotoDbEntityToDomainModel(): Photo = Photo(photoId, title, url)