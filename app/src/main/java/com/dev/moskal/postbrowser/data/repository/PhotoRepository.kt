package com.dev.moskal.postbrowser.data.repository

import com.dev.moskal.postbrowser.data.db.DbPhoto
import com.dev.moskal.postbrowser.data.network.api.PhotoApi
import com.dev.moskal.postbrowser.data.network.response.PhotoApiResponse

internal class PhotoRepository constructor(
    private val photoApi: PhotoApi,
    private val mapApiResponseToDbEntity: List<PhotoApiResponse>.() -> List<DbPhoto>,
) {
    suspend fun fetchData(): List<DbPhoto> = photoApi.getPhotos().mapApiResponseToDbEntity()
}