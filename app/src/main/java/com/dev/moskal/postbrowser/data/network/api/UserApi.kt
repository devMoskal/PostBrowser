package com.dev.moskal.postbrowser.data.network.api

import com.dev.moskal.postbrowser.data.network.response.UserApiResponse
import retrofit2.http.GET

interface UserApi {

    @GET(PATH_USERS)
    suspend fun getUsers(): List<UserApiResponse>

    private companion object {
        private const val PATH_USERS = "users"
    }
}