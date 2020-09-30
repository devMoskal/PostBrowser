package com.dev.moskal.postbrowser.data.repository

import com.dev.moskal.postbrowser.BaseTest
import com.dev.moskal.postbrowser.coCalledOnce
import com.dev.moskal.postbrowser.coWasNotCalled
import com.dev.moskal.postbrowser.data.db.PostBrowserDao
import com.dev.moskal.postbrowser.domain.Repository
import com.dev.moskal.postbrowser.domain.model.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import retrofit2.HttpException


internal class MainRepositoryTest : BaseTest() {

    @MockK
    internal lateinit var mockPostRepository: PostRepository

    @MockK
    internal lateinit var mockUserRepository: UserRepository

    @MockK
    internal lateinit var mockAlbumRepository: AlbumRepository

    @MockK
    internal lateinit var mockPhotoRepository: PhotoRepository

    @RelaxedMockK
    internal lateinit var mockDao: PostBrowserDao


    private lateinit var repository: Repository

    @BeforeEach
    fun init() {
        repository = MainRepository(
            mockDao,
            mockPostRepository,
            mockUserRepository,
            mockAlbumRepository,
            mockPhotoRepository,
            { map { mockk() } }
        ) { mockk() }
    }

    @Nested
    inner class FetchDataTests {
        @Test
        fun `when all repository fetched data without errors then batch update database`() =
            runBlocking {
                // given
                coEvery { mockPostRepository.fetchData() } returns mockk()
                coEvery { mockUserRepository.fetchData() } returns mockk()
                coEvery { mockAlbumRepository.fetchData() } returns mockk()
                coEvery { mockPhotoRepository.fetchData() } returns mockk()
                // when
                val result = repository.fetchData()
                // then
                assertThat(result).isInstanceOf(Resource.Success::class.java)
                coCalledOnce { mockDao.batchUpdate(any(), any(), any(), any()) }
            }

        @Test
        fun `when post repository fetched fails then do not update database`() = runBlocking {
            // given
            coEvery { mockPostRepository.fetchData() } throws HttpException(mockk(relaxed = true))
            coEvery { mockUserRepository.fetchData() } returns mockk()
            coEvery { mockAlbumRepository.fetchData() } returns mockk()
            coEvery { mockPhotoRepository.fetchData() } returns mockk()
            // when
            val result = repository.fetchData()
            // then
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            coWasNotCalled { mockDao.batchUpdate(any(), any(), any(), any()) }
        }

        @Test
        fun `when user repository fetched fails then do not update database`() = runBlocking {
            // given
            coEvery { mockPostRepository.fetchData() } returns mockk()
            coEvery { mockUserRepository.fetchData() } throws HttpException(mockk(relaxed = true))
            coEvery { mockAlbumRepository.fetchData() } returns mockk()
            coEvery { mockPhotoRepository.fetchData() } returns mockk()

            // when
            val result = repository.fetchData()
            // then
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            coWasNotCalled { mockDao.batchUpdate(any(), any(), any(), any()) }
        }

        @Test
        fun `when album repository fetched fails then do not update database`() = runBlocking {
            // given
            coEvery { mockPostRepository.fetchData() } returns mockk()
            coEvery { mockUserRepository.fetchData() } returns mockk()
            coEvery { mockAlbumRepository.fetchData() } throws HttpException(mockk(relaxed = true))
            coEvery { mockPhotoRepository.fetchData() } returns mockk()
            // when
            val result = repository.fetchData()
            // then
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            coWasNotCalled { mockDao.batchUpdate(any(), any(), any(), any()) }
        }

        @Test
        fun `when photo repository fetched fails then do not update database`() = runBlocking {
            // given
            coEvery { mockPostRepository.fetchData() } returns mockk()
            coEvery { mockUserRepository.fetchData() } returns mockk()
            coEvery { mockAlbumRepository.fetchData() } returns mockk()
            coEvery { mockPhotoRepository.fetchData() } throws HttpException(mockk(relaxed = true))

            // when
            val result = repository.fetchData()
            // then
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            coWasNotCalled { mockDao.batchUpdate(any(), any(), any(), any()) }
        }

        @Test
        fun `when dto update fails then propagate error`() = runBlocking {
            // given
            coEvery { mockPostRepository.fetchData() } returns mockk()
            coEvery { mockUserRepository.fetchData() } returns mockk()
            coEvery { mockAlbumRepository.fetchData() } returns mockk()
            coEvery { mockPhotoRepository.fetchData() } returns mockk()
            coEvery { mockDao.batchUpdate(any(), any(), any(), any()) } throws RuntimeException()
            // when
            val result = repository.fetchData()
            // then
            coCalledOnce { mockDao.batchUpdate(any(), any(), any(), any()) }
            assertThat(result).isInstanceOf(Resource.Error::class.java)
        }
    }

    @Nested
    inner class PostTests {
        @Test
        fun `when post repo returns fix number of post then get all of them`() = runBlocking {
            // given
            every { mockPostRepository.getPostsInfo() } returns flowOf(
                listOf(
                    mockk(relaxed = true),
                    mockk(relaxed = true)
                )
            )

            // when
            val posts = repository.getPostsInfo().toList()

            // then
            assertThat(posts).isNotEmpty()
            assertThat(posts[0].isSuccess())
            assertThat((posts[0] as Resource.Success).data).hasSize(2)
        }

        @Test
        fun `when post repo emits several responses then get all of them`() = runBlocking {
            // given
            every { mockPostRepository.getPostsInfo() } returns flowOf(
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
            coEvery { mockPostRepository.getPostsInfo() } throws RuntimeException("")

            // then
            assertThrows<RuntimeException> {
                runBlocking {
                    repository.getPostsInfo().toList()
                }
            }
        }
    }
}