@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.dev.moskal.postbrowser.data.repository

import com.dev.moskal.postbrowser.data.db.DbPostWithUser
import com.dev.moskal.postbrowser.data.db.PostBrowserDao
import com.dev.moskal.postbrowser.domain.Repository
import com.dev.moskal.postbrowser.domain.model.PostInfo
import com.dev.moskal.postbrowser.domain.model.asResource
import com.dev.moskal.postbrowser.domain.model.asResourceFlow
import kotlinx.coroutines.flow.map

class RepositoryImpl(
    private val dao: PostBrowserDao,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val mapPostInfoDbEntityToDomainModelWithUser: List<DbPostWithUser>.() -> List<PostInfo>
) : Repository {

    /*
     * Batch update to db is preventing form having a corrupted db
     * when some api call or db insertion fails
     */
    override suspend fun fetchData() = asResource {
        val posts = postRepository.fetchData()
        val users = userRepository.fetchData()
        dao.batchUpdate(posts, users)
    }

    override fun getPostsInfo() = postRepository.getPostsInfo()
        .map(mapPostInfoDbEntityToDomainModelWithUser::invoke)
        .asResourceFlow()

    override suspend fun deletePost(id: Int) = asResource {
        postRepository.delete(id)
    }
}