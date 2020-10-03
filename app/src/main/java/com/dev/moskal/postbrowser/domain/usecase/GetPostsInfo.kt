package com.dev.moskal.postbrowser.domain.usecase

import androidx.paging.PagingData
import com.dev.moskal.postbrowser.domain.Repository
import com.dev.moskal.postbrowser.domain.model.PostInfo
import com.dev.moskal.postbrowser.domain.model.SyncState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetPostsInfo @Inject constructor(
    private val repository: Repository,
    private val fetchData: FetchData
) {

    fun execute(): Flow<PagingData<PostInfo>> =
        fetchData.syncState
            .filterNot { it == SyncState.NOT_STARTED || it == SyncState.IN_PROGRESS }
            .flatMapLatest { repository.getPostsInfo() }
}