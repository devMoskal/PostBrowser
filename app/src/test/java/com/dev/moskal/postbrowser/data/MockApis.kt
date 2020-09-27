package com.dev.moskal.postbrowser.data

import com.dev.moskal.postbrowser.data.network.api.PostApi
import com.dev.moskal.postbrowser.data.network.response.PostApiResponse

internal class SuccessMockPostApi : PostApi {
    override suspend fun getPosts(): List<PostApiResponse> = listOf(
        PostApiResponse(1, 1, "test1", "test1"),
        PostApiResponse(2, 2, "test2", "test2"),
    )
}
