package com.dev.moskal.postbrowser.domain

import androidx.paging.PagingData
import com.dev.moskal.postbrowser.domain.model.Album
import com.dev.moskal.postbrowser.domain.model.Post
import com.dev.moskal.postbrowser.domain.model.PostInfo
import com.dev.moskal.postbrowser.domain.model.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Calls [getPostsInfo] and [getPost] is reactive as post can be deleted form list.
 * To showcase diversity of solution and as data it query is immutable [getUserAlbums]
 * is just regular suspend function. Yet it could as well be reactive,
 * and in production it most likely would be a way to go due to extendability.
 */
interface Repository {
    suspend fun fetchData(): Resource<Unit>

    fun getPostsInfo(): Flow<PagingData<PostInfo>>
    fun getPost(id: Int): Flow<Resource<Post?>>

    suspend fun deletePost(id: Int): Resource<Unit>

    // This call could be flow as well, yet to demonstrate different approach
    // and as Albums are immutable I've decide to make it regular suspend fun
    suspend fun getUserAlbums(userId: Int): Resource<List<Album>>
}