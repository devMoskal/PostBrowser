package com.dev.moskal.postbrowser.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
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
    private val mapPostInfoDbEntityToDomainModelWithUser: DbPostWithUser.() -> PostInfo,
    private val mapPostDbEntityToDomainModelWithUser: DbPost?.() -> Post?
) {
    suspend fun fetchData(): List<DbPost> = postApi.getPosts().mapApiResponseToDbEntity()

    fun getPostsInfo() = Pager(
        config = PagingConfig(
            pageSize = 15,
            enablePlaceholders = false
        ),
        pagingSourceFactory = dao.getPostWithUser().asPagingSourceFactory(),
    ).flow.map { pagingData ->
        pagingData.map(mapPostInfoDbEntityToDomainModelWithUser::invoke)
    }

    fun getPost(id: Int) = dao.getPost(id)
        .map(mapPostDbEntityToDomainModelWithUser::invoke)
        .distinctUntilChanged()

    suspend fun delete(id: Int) = dao.deletePost(id)
}