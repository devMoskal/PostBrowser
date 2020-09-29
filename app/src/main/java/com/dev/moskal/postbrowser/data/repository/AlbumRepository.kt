package com.dev.moskal.postbrowser.data.repository

import com.dev.moskal.postbrowser.data.db.DbAlbum
import com.dev.moskal.postbrowser.data.network.api.AlbumApi
import com.dev.moskal.postbrowser.data.network.response.AlbumApiResponse

internal class AlbumRepository constructor(
    private val albumApi: AlbumApi,
    private val mapApiResponseToDbEntity: List<AlbumApiResponse>.() -> List<DbAlbum>,
) {
    suspend fun fetchData(): List<DbAlbum> = albumApi.getAlbums().mapApiResponseToDbEntity()
}