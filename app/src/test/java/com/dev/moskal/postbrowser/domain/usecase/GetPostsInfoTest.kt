package com.dev.moskal.postbrowser.domain.usecase

import androidx.paging.PagingData
import com.dev.moskal.postbrowser.BaseTest
import com.dev.moskal.postbrowser.coCalledOnce
import com.dev.moskal.postbrowser.coWasNotCalled
import com.dev.moskal.postbrowser.domain.Repository
import com.dev.moskal.postbrowser.domain.model.PostInfo
import com.dev.moskal.postbrowser.domain.model.SyncState
import com.dev.moskal.postbrowser.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("EXPERIMENTAL_API_USAGE")
internal class GetPostsInfoTest : BaseTest() {

    @MockK
    internal lateinit var mockRepository: Repository

    @RelaxedMockK
    internal lateinit var fetchData: FetchData

    private lateinit var getPostsInfo: GetPostsInfo

    @BeforeEach
    fun init() {
        getPostsInfo = GetPostsInfo(mockRepository, fetchData)
    }

    @Test
    fun `when sync was not started then do not query repository`() = runBlockingTest {
        // given
        coEvery { fetchData.syncState.value } returns SyncState.NOT_STARTED
        // when
        val result = getPostsInfo.execute("").toList()
        // then
        assertThat(result).isEmpty()
        coWasNotCalled { mockRepository.getPostsInfo(any()) }
    }

    @Test
    fun `when sync is in progress then do not query repository`() = runBlockingTest {
        // given
        coEvery { fetchData.syncState.value } returns SyncState.IN_PROGRESS
        // when
        val result = getPostsInfo.execute("").toList()
        // then
        assertThat(result).isEmpty()
        coWasNotCalled { mockRepository.getPostsInfo(any()) }
    }

    @Test
    fun `when sync is finished then query repository and emit responses`() = runBlockingTest {
        // given
        val repositoryData = PagingData.from(mockk<List<PostInfo>>())
        val subject = MutableStateFlow(SyncState.NOT_STARTED)
        coEvery { fetchData.syncState } returns subject
        coEvery { mockRepository.getPostsInfo("") } returns flowOf(repositoryData)
        // when - then

        val observer = getPostsInfo.execute("").test(this)
        observer.assertNoValues()
        coWasNotCalled { mockRepository.getPostsInfo(any()) }

        subject.value = SyncState.IN_PROGRESS
        observer.assertNoValues()
        coWasNotCalled { mockRepository.getPostsInfo(any()) }

        subject.value = SyncState.SUCCESS

        // then
        coCalledOnce { mockRepository.getPostsInfo("") }
        observer.assertValues(repositoryData)
        observer.finish()
    }

}