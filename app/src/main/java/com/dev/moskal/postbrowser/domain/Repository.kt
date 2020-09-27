package com.dev.moskal.postbrowser.domain

import com.dev.moskal.postbrowser.domain.model.Post
import com.dev.moskal.postbrowser.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun fetchData(): Resource<Unit>
    fun getPosts(): Flow<Resource<List<Post>>>
}