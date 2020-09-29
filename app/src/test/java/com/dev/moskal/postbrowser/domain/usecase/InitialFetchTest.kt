package com.dev.moskal.postbrowser.domain.usecase

import com.dev.moskal.postbrowser.BaseTest
import com.dev.moskal.postbrowser.coCalledOnce
import com.dev.moskal.postbrowser.coWasNotCalled
import com.dev.moskal.postbrowser.domain.model.SyncState
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("EXPERIMENTAL_API_USAGE")
internal class InitialFetchTest : BaseTest() {

    @RelaxedMockK
    internal lateinit var fetchData: FetchData

    private lateinit var initialFetch: InitialFetch

    @BeforeEach
    fun init() {
        initialFetch = InitialFetch(fetchData)
    }

    @Test
    fun `when sync was not started then start sync on call`() = runBlockingTest {
        // given
        coEvery { fetchData.syncState.value } returns SyncState.NOT_STARTED
        // when
        initialFetch.execute()
        // then
        coCalledOnce { fetchData.execute() }
    }

    @Test
    fun `when sync was already started then do not start sync for the second time`() =
        runBlockingTest {
            // given
            coEvery { fetchData.syncState.value } returns SyncState.SUCCESS
            // when
            initialFetch.execute()
            // then
            coWasNotCalled { fetchData.execute() }
        }
}