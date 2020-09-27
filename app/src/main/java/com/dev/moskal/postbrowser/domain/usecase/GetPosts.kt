package com.dev.moskal.postbrowser.domain.usecase

import com.dev.moskal.postbrowser.domain.Repository
import com.dev.moskal.postbrowser.domain.model.Post
import com.dev.moskal.postbrowser.domain.model.Resource
import com.dev.moskal.postbrowser.domain.model.SyncState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetPosts @Inject constructor(
    private val repository: Repository,
    private val fetchData: FetchData
) {

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun execute(): Flow<Resource<List<Post>>> =
        fetchData.syncState
            .filterNot { it == SyncState.NOT_STARTED || it == SyncState.IN_PROGRESS }
            .flatMapLatest { repository.getPosts() }
}