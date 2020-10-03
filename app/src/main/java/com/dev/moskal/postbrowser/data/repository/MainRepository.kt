@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.dev.moskal.postbrowser.data.repository

import com.dev.moskal.postbrowser.data.db.PostBrowserDao
import com.dev.moskal.postbrowser.domain.Repository
import com.dev.moskal.postbrowser.domain.model.asResource
import com.dev.moskal.postbrowser.domain.model.asResourceFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

internal class MainRepository(
    private val dao: PostBrowserDao,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val albumRepository: AlbumRepository,
    private val photoRepository: PhotoRepository,
) : Repository {

    /*
     * Batch update to db is preventing form having a corrupted db
     * when one of the api call or db insertion fails.
     */
    override suspend fun fetchData() = asResource {
        withContext(Dispatchers.IO) {
            val posts = async { postRepository.fetchData() }
            val users = async { userRepository.fetchData() }
            val albums = async { albumRepository.fetchData() }
            val photos = async { photoRepository.fetchData() }
            dao.batchUpdate(posts.await(), users.await(), albums.await(), photos.await())
        }
    }

    override fun getPostsInfo() = postRepository.getPostsInfo()


    override fun getPost(id: Int) = postRepository.getPost(id).asResourceFlow()

    override suspend fun getUserAlbums(userId: Int) = asResource {
        albumRepository.getAlbumsForUser(userId)
    }

    override suspend fun deletePost(id: Int) = asResource {
        postRepository.delete(id)
    }
}