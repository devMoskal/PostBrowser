package com.dev.moskal.postbrowser.data.repository

import com.dev.moskal.postbrowser.data.db.DbPost
import com.dev.moskal.postbrowser.data.db.PostBrowserDao
import com.dev.moskal.postbrowser.data.network.api.PostApi
import com.dev.moskal.postbrowser.data.network.response.PostApiResponse

class PostRepository constructor(
    private val postApi: PostApi,
    private val dao: PostBrowserDao,
    private val mapApiResponseToDbEntity: List<PostApiResponse>.() -> List<DbPost>,
) {
    suspend fun fetchData(): List<DbPost> = postApi.getPosts().mapApiResponseToDbEntity()

    fun getPostsInfo() = dao.getPostsInfo()
}