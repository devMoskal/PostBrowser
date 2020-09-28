package com.dev.moskal.postbrowser.domain.usecase

import com.dev.moskal.postbrowser.BaseTest
import com.dev.moskal.postbrowser.coCalledOnce
import com.dev.moskal.postbrowser.domain.Repository
import com.dev.moskal.postbrowser.domain.model.Resource
import com.dev.moskal.postbrowser.domain.model.SyncState
import com.google.common.truth.Truth.assertThat
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("EXPERIMENTAL_API_USAGE")
internal class FetchDataTest : BaseTest() {
    @MockK
    internal lateinit var mockRepository: Repository

    private lateinit var fetchData: FetchData

    @BeforeEach
    fun init() {
        fetchData = FetchData(mockRepository)
    }

    @Test
    fun `when getting sync state before fetching then return not started state`() =
        runBlockingTest {
            // given
            // when
            val result = fetchData.syncState.value
            // then
            assertThat(result).isEqualTo(SyncState.NOT_STARTED)
            verify { mockRepository wasNot Called }
        }

    @Test
    fun `when fetchData returns success then return success sync state`() = runBlockingTest {
        // given
        coEvery { mockRepository.fetchData() } returns Resource.Success(Unit)
        // when
        fetchData.execute()
        val result = fetchData.syncState.value
        // then
        assertThat(result).isEqualTo(SyncState.SUCCESS)
        coCalledOnce { mockRepository.fetchData() }
    }

    @Test
    fun `when fetchData returns error then return error sync state`() = runBlockingTest {
        // given
        coEvery { mockRepository.fetchData() } returns Resource.Error(mockk())
        // when
        this.pauseDispatcher()
        fetchData.execute()
        val result = fetchData.syncState.value
        // then
        assertThat(result).isEqualTo(SyncState.ERROR)
        coCalledOnce { mockRepository.fetchData() }
    }
}