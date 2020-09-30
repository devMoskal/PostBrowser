package com.dev.moskal.postbrowser.domain.usecase

import com.dev.moskal.postbrowser.BaseTest
import com.dev.moskal.postbrowser.coCalledOnce
import com.dev.moskal.postbrowser.domain.Repository
import com.dev.moskal.postbrowser.domain.model.Post
import com.dev.moskal.postbrowser.domain.model.Resource
import com.dev.moskal.postbrowser.isResourceError
import com.dev.moskal.postbrowser.isResourceSuccess
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("EXPERIMENTAL_API_USAGE")
internal class GetPostTest : BaseTest() {

    @MockK
    internal lateinit var mockRepository: Repository

    private lateinit var getPost: GetPost

    @BeforeEach
    fun init() {
        getPost = GetPost(mockRepository)
    }

    @Test
    fun `when repository return success then propagate propagate result`() = runBlockingTest {
        // given
        val data = mockk<Post>()
        coEvery { mockRepository.getPost(any()) } returns Resource.Success(data)
        // when
        val result = getPost.execute(42)
        // then
        assertThat(result).isResourceSuccess()
        assertThat(result.data).isEqualTo(data)
        coCalledOnce { mockRepository.getPost(42) }
    }

    @Test
    fun `when repository return error then propagate propagate result`() = runBlockingTest {
        // given
        val error = mockk<Exception>()
        coEvery { mockRepository.getPost(any()) } returns Resource.Error(error)
        // when
        val result = getPost.execute(42)
        // then
        assertThat(result).isResourceError()
        assertThat((result as Resource.Error).error).isEqualTo(error)
        coCalledOnce { mockRepository.getPost(42) }
    }
}