package com.dev.moskal.postbrowser.data.network.api

import com.dev.moskal.postbrowser.data.network.response.PostApiResponse
import retrofit2.http.GET

interface PostApi {

    @GET(PATH_POSTS)
    suspend fun getPosts(): List<PostApiResponse>

    private companion object {
        private const val PATH_POSTS = "posts"
    }
}