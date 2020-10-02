package com.dev.moskal.postbrowser.domain.usecase

import com.dev.moskal.postbrowser.domain.model.SyncState
import javax.inject.Inject

/**
 * Ensure that fetching is done only once per cold start.
 *
 * Sample app note:
 * This is only one from many ways of ensuring cold start
 * with most obvious alternative being Splash Screen.
 * Yet with this solution we can avoid extra screen, and load data in non-blocking matter.
 * It's also flexible - in case of a need of adding Splash screen later on
 * we can just invoke this Use Case from there and we are good to go.
 */
class InitialFetch @Inject constructor(private val fetchData: FetchData) {

    suspend fun execute() {
        if (fetchData.syncState.value == SyncState.NOT_STARTED) {
            fetchData.execute()
        }
    }
}