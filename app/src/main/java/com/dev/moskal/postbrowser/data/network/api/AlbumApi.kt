package com.dev.moskal.postbrowser.data.network.api

import com.dev.moskal.postbrowser.data.network.response.AlbumApiResponse
import retrofit2.http.GET

interface AlbumApi {

    @GET(PATH_ALBUMS)
    suspend fun getAlbums(): List<AlbumApiResponse>

    private companion object {
        private const val PATH_ALBUMS = "albums"
    }
}