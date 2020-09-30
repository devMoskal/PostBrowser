package com.dev.moskal.postbrowser.domain

import com.dev.moskal.postbrowser.domain.model.Album
import com.dev.moskal.postbrowser.domain.model.Post
import com.dev.moskal.postbrowser.domain.model.PostInfo
import com.dev.moskal.postbrowser.domain.model.Resource
import kotlinx.coroutines.flow.Flow

/**
 * List of Posts form [getPostsInfo] is reactive as post can be deleted form list.
 * Rest of calls to showcase diversity of solution and for simplicity are just regular coroutines,
 * - as data they querying are immutable. Yet they could as well be flows, same as [getPostsInfo].
 */
interface Repository {
    suspend fun fetchData(): Resource<Unit>

    fun getPostsInfo(): Flow<Resource<List<PostInfo>>>

    suspend fun getPost(id: Int): Resource<Post>
    suspend fun deletePost(id: Int): Resource<Unit>
    suspend fun getUserAlbums(userId: Int): Resource<List<Album>>
}