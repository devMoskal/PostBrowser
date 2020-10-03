package com.dev.moskal.postbrowser.data.repository

import com.dev.moskal.postbrowser.data.db.DbPost
import com.dev.moskal.postbrowser.data.db.DbPostWithUser
import com.dev.moskal.postbrowser.data.db.PostBrowserDao
import com.dev.moskal.postbrowser.data.network.api.PostApi
import com.dev.moskal.postbrowser.data.network.response.PostApiResponse
import com.dev.moskal.postbrowser.domain.model.Post
import com.dev.moskal.postbrowser.domain.model.PostInfo
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class PostRepository constructor(
    private val postApi: PostApi,
    private val dao: PostBrowserDao,
    private val mapApiResponseToDbEntity: List<PostApiResponse>.() -> List<DbPost>,
    private val mapPostInfoDbEntityToDomainModelWithUser: List<DbPostWithUser>.() -> List<PostInfo>,
    private val mapPostDbEntityToDomainModelWithUser: DbPost?.() -> Post?
) {
    suspend fun fetchData(): List<DbPost> = postApi.getPosts().mapApiResponseToDbEntity()

    fun getPostsInfo() = dao.getPostWithUser()
        .map(mapPostInfoDbEntityToDomainModelWithUser::invoke)
        .distinctUntilChanged()

    fun getPost(id: Int) = dao.getPost(id)
        .map(mapPostDbEntityToDomainModelWithUser::invoke)
        .distinctUntilChanged()

    suspend fun delete(id: Int) = dao.deletePost(id)
}