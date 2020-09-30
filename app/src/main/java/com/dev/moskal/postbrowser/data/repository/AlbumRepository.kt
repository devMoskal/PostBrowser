package com.dev.moskal.postbrowser.data.repository

import com.dev.moskal.postbrowser.data.db.DbAlbum
import com.dev.moskal.postbrowser.data.db.DbAlbumWithPhotos
import com.dev.moskal.postbrowser.data.db.PostBrowserDao
import com.dev.moskal.postbrowser.data.network.api.AlbumApi
import com.dev.moskal.postbrowser.data.network.response.AlbumApiResponse
import com.dev.moskal.postbrowser.domain.model.Album

internal class AlbumRepository constructor(
    private val albumApi: AlbumApi,
    private val dao: PostBrowserDao,
    private val mapApiResponseToDbEntity: List<AlbumApiResponse>.() -> List<DbAlbum>,
    private val mapDbEntityToDomainModel: List<DbAlbumWithPhotos>.() -> List<Album>
) {
    suspend fun fetchData(): List<DbAlbum> = albumApi.getAlbums().mapApiResponseToDbEntity()

    suspend fun getAlbumsForUser(userId: Int) =
        dao.getAlbumsWithPhoto(userId).mapDbEntityToDomainModel()
}