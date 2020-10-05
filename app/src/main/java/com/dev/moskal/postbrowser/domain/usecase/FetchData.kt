package com.dev.moskal.postbrowser.domain.usecase

import com.dev.moskal.postbrowser.domain.Repository
import com.dev.moskal.postbrowser.domain.model.Resource
import com.dev.moskal.postbrowser.domain.model.SyncState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchData @Inject constructor(private val repository: Repository) {
    private val _syncState = MutableStateFlow(SyncState.NOT_STARTED)
    val syncState: StateFlow<SyncState> = _syncState

    suspend fun execute(): Resource<Unit> {
        _syncState.value = SyncState.IN_PROGRESS
        return repository.fetchData()
            .also { result: Resource<Unit> ->
                _syncState.value = when (result) {
                    is Resource.Success -> SyncState.SUCCESS
                    is Resource.Error -> {
                        Timber.e(result.error, "Failed to fetch data")
                        SyncState.ERROR
                    }
                }
            }
    }
}