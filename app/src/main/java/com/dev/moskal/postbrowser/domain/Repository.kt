package com.dev.moskal.postbrowser.domain

import com.dev.moskal.postbrowser.domain.model.PostInfo
import com.dev.moskal.postbrowser.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun fetchData(): Resource<Unit>
    fun getPosts(): Flow<Resource<List<PostInfo>>>
}