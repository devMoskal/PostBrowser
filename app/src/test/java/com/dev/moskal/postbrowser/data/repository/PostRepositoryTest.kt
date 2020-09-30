package com.dev.moskal.postbrowser.data.repository

import com.dev.moskal.postbrowser.BaseTest
import com.dev.moskal.postbrowser.data.db.PostBrowserDao
import com.dev.moskal.postbrowser.data.network.api.PostApi
import com.dev.moskal.postbrowser.domain.model.PostInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
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
            { map { mockk() } },
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
    fun `when dao returns fix number of post then get all of them`() = runBlockingTest {
        // given
        every { mockDao.getPostWithUser() } returns flowOf(
            listOf(
                mockk(relaxed = true),
                mockk(relaxed = true)
            )
        )

        // when
        val posts = repository.getPostsInfo().toList()

        // then
        assertThat(posts).isNotEmpty()
        assertThat(posts[0][0]).isInstanceOf(PostInfo::class.java)
    }

    @Test
    fun `when dao emits several responses then get all of them`() = runBlockingTest {
        // given
        every { mockDao.getPostWithUser() } returns flowOf(
            listOf(mockk(relaxed = true), mockk(relaxed = true)),
            listOf(mockk(relaxed = true), mockk(relaxed = true)),
            listOf(mockk(relaxed = true)),
            emptyList(),
            listOf(mockk(relaxed = true))
        )

        // when
        val posts = repository.getPostsInfo().toList()

        // then
        assertThat(posts).hasSize(5)
    }

    @Test
    fun `when dao throws error then propagate error response`() {
        // given
        coEvery { mockService.getPosts() } throws RuntimeException("")

        // then
        assertThrows<RuntimeException> {
            runBlockingTest {
                repository.getPostsInfo().toList()
            }
        }
    }
}
