package com.dev.moskal.postbrowser.data.repository

import com.dev.moskal.postbrowser.BaseTest
import com.dev.moskal.postbrowser.data.db.PostBrowserDao
import com.dev.moskal.postbrowser.data.network.api.PostApi
import com.google.common.truth.Truth.assertThat
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import retrofit2.HttpException

/*
 * As other repo test would be very similar I've omitted them in sample app
 */
@Suppress("EXPERIMENTAL_API_USAGE")
internal class PostRepositoryTest : BaseTest() {

    @MockK
    internal lateinit var mockService: PostApi

    @MockK
    internal lateinit var mockDao: PostBrowserDao

    private lateinit var repository: PostRepository

    @BeforeEach
    fun init() {
        repository = PostRepository(
            mockService,
            mockDao,
            { map { mockk() } },
            { mockk() },
        ) { mockk() }
    }

    @Test
    fun `when api returns fix number of elements then fetch all of them`() = runBlockingTest {
        // given
        coEvery { mockService.getPosts() } returns listOf(mockk(), mockk())
        // when
        val result = repository.fetchData()
        // then
        assertThat(result).isInstanceOf(List::class.java)
        assertThat(result).hasSize(2)
        verify { mockDao wasNot Called }
    }

    @Test
    fun `when api returns empty list then fetch empty list`() = runBlockingTest {
        // given
        coEvery { mockService.getPosts() } returns emptyList()
        // when
        val result = repository.fetchData()
        // then
        assertThat(result).isInstanceOf(List::class.java)
        assertThat(result).isEmpty()
        verify { mockDao wasNot Called }
    }

    @Test
    fun `when api throws error then propagate error response`() {
        // given
        coEvery { mockService.getPosts() } throws HttpException(mockk(relaxed = true))
        // then
        assertThrows<HttpException> {
            runBlockingTest {
                repository.fetchData()
            }
        }
    }

    @Test
    fun `when dao throws error then propagate error response`() {
        // given
        coEvery { mockService.getPosts() } throws RuntimeException("")

        // then
        assertThrows<RuntimeException> {
            runBlockingTest {
                repository.getPostsInfo("").toList()
            }
        }
    }
}
