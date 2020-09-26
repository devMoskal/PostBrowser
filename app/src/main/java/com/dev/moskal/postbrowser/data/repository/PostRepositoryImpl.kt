package com.dev.moskal.postbrowser.data.repository

import com.dev.moskal.postbrowser.data.network.api.PostApi
import com.dev.moskal.postbrowser.data.network.response.PostApiResponse
import com.dev.moskal.postbrowser.domain.model.Post
import com.dev.moskal.postbrowser.domain.model.Resource
import com.dev.moskal.postbrowser.domain.repository.PostRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PostRepositoryImpl(
    private val postApi: PostApi,
    private val postMapper: (List<PostApiResponse>) -> List<Post>,
) : PostRepository {

    override fun getPosts() =
        flow { emit(postApi.getPosts()) }
            .map { postMapper.invoke(it) }
            .map { Resource.Success(it) as Resource<List<Post>> }
            .catch {
                emit(Resource.Error(it))
            }
}