package com.dev.moskal.postbrowser.domain.usecase

import com.dev.moskal.postbrowser.domain.Repository
import com.dev.moskal.postbrowser.domain.model.Resource
import com.dev.moskal.postbrowser.domain.model.SyncState
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("EXPERIMENTAL_API_USAGE")
@Singleton
class FetchData @Inject constructor(private val repository: Repository) {
    private val _syncState: BroadcastChannel<SyncState> =
        ConflatedBroadcastChannel(SyncState.NOT_STARTED)
    val syncState = _syncState.asFlow()

    suspend fun execute(): Resource<Unit> {
        _syncState.send(SyncState.IN_PROGRESS)
        return repository.fetchData()
            .also { result: Resource<Unit> ->
                _syncState.send(
                    when (result) {
                        is Resource.Success -> SyncState.SUCCESS
                        is Resource.Error -> SyncState.ERROR
                    }
                )
            }
    }
}