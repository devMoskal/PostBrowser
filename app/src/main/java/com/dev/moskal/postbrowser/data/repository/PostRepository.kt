package com.dev.moskal.postbrowser.data.repository

import com.dev.moskal.postbrowser.data.db.DbPost
import com.dev.moskal.postbrowser.data.db.PostBrowserDao
import com.dev.moskal.postbrowser.data.network.api.PostApi
import com.dev.moskal.postbrowser.data.network.response.PostApiResponse
import com.dev.moskal.postbrowser.domain.model.Post
import kotlinx.coroutines.flow.map

class PostRepository constructor(
    private val postApi: PostApi,
    private val dao: PostBrowserDao,
    private val mapApiResponseToDomainModel: List<PostApiResponse>.() -> List<Post>,
    private val mapDomainModelToDbEntity: List<Post>.() -> List<DbPost>,
    private val mapDbEntityToDomainModel: List<DbPost>.() -> List<Post>,
) {
    suspend fun fetchData(): List<DbPost> =
        postApi.getPosts()
            .mapApiResponseToDomainModel()
            .mapDomainModelToDbEntity()

    fun getPosts() =
        dao.getPosts()
            .map(mapDbEntityToDomainModel::invoke)
}