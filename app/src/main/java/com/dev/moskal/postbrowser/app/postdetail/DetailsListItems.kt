package com.dev.moskal.postbrowser.app.postdetail


import com.dev.moskal.postbrowser.app.postdetail.AlbumListViewTypes.TYPE_ALBUM
import com.dev.moskal.postbrowser.app.postdetail.AlbumListViewTypes.TYPE_ALBUMS_LABEL
import com.dev.moskal.postbrowser.app.postdetail.AlbumListViewTypes.TYPE_DETAILS
import com.dev.moskal.postbrowser.app.postdetail.AlbumListViewTypes.TYPE_LOADING
import com.dev.moskal.postbrowser.app.postdetail.AlbumListViewTypes.TYPE_PHOTO
import com.dev.moskal.postbrowser.domain.model.Album
import com.dev.moskal.postbrowser.domain.model.Photo
import com.dev.moskal.postbrowser.domain.model.Post

object AlbumListViewTypes {
    internal const val TYPE_LOADING = 0
    internal const val TYPE_DETAILS = 1
    internal const val TYPE_ALBUM = 2
    internal const val TYPE_PHOTO = 3
    internal const val TYPE_ALBUMS_LABEL = 4
}

sealed class DetailsListItem(val type: Int) {

    data class HeaderItem(
        val title: String,
        val body: String
    ) : DetailsListItem(TYPE_DETAILS) {
        constructor(post: Post) : this(
            post.title,
            post.body,
        )
    }

    data class AlbumItem(
        val id: Int,
        val title: String,
        val isExpanded: Boolean = false
    ) : DetailsListItem(TYPE_ALBUM) {
        constructor(album: Album) : this(
            album.id,
            album.title,
        )
    }

    data class PhotoItem(
        val id: Int,
        val title: String,
        val url: String,
        val isVisible: Boolean
    ) : DetailsListItem(TYPE_PHOTO) {
        constructor(photo: Photo, isVisible: Boolean) : this(
            photo.id,
            photo.title,
            photo.url,
            isVisible
        )
    }

    object AlbumsLabelItem : DetailsListItem(TYPE_ALBUMS_LABEL)

    object AlbumLoadingItem : DetailsListItem(TYPE_LOADING)
}