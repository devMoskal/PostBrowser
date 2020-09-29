package com.dev.moskal.postbrowser.data.network.api

import com.dev.moskal.postbrowser.data.network.response.PhotoApiResponse
import retrofit2.http.GET

interface PhotoApi {

    @GET(PATH_PHOTOS)
    suspend fun getPhotos(): List<PhotoApiResponse>

    private companion object {
        private const val PATH_PHOTOS = "photos"
    }
}