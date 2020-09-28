@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.dev.moskal.postbrowser.data.repository

import com.dev.moskal.postbrowser.data.db.DbPostAndUser
import com.dev.moskal.postbrowser.data.db.PostBrowserDao
import com.dev.moskal.postbrowser.domain.Repository
import com.dev.moskal.postbrowser.domain.model.PostInfo
import com.dev.moskal.postbrowser.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class RepositoryImpl(
    private val dao: PostBrowserDao,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val mapPostInfoDbEntityToDomainModelAndUser: List<DbPostAndUser>.() -> List<PostInfo>
) : Repository {

    /*
     * Batch update to db is preventing form having a corrupted db
     * when some api call or db insertion fails
     */
    override suspend fun fetchData() =
        try {
            val posts = postRepository.fetchData()
            val users = userRepository.fetchData()
            dao.batchUpdate(posts, users)
            Resource.Success(Unit)
        } catch (error: Throwable) {
            Resource.Error(error)
        }

    override fun getPosts() = postRepository.getPostsInfo()
        .map(mapPostInfoDbEntityToDomainModelAndUser::invoke)
        .toResourceFlow()

    private fun <T> Flow<T>.toResourceFlow(): Flow<Resource<T>> = this
        .map { Resource.Success(it) as Resource<T> }
        .catch {
            emit(Resource.Error(it))
        }
}