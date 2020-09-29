package com.dev.moskal.postbrowser.domain.usecase

import com.dev.moskal.postbrowser.BaseTest
import com.dev.moskal.postbrowser.coCalledOnce
import com.dev.moskal.postbrowser.domain.Repository
import com.dev.moskal.postbrowser.domain.model.Resource
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("EXPERIMENTAL_API_USAGE")
internal class DeletePostTest : BaseTest() {

    @MockK
    internal lateinit var mockRepository: Repository

    private lateinit var deletePost: DeletePost

    @BeforeEach
    fun init() {
        deletePost = DeletePost(mockRepository)
    }

    @Test
    fun `when repository return success then propagate propagate result`() = runBlockingTest {
        // given
        coEvery { mockRepository.deletePost(any()) }.returns(Resource.Success(mockk()))
        // when
        val result = deletePost.execute(42)
        // then
        Truth.assertThat(result).isInstanceOf(Resource.Success::class.java)
        coCalledOnce { mockRepository.deletePost(42) }
    }

    @Test
    fun `when repository return error then propagate propagate result`() = runBlockingTest {
        // given
        coEvery { mockRepository.deletePost(any()) }.returns(Resource.Error(mockk()))
        // when
        val result = deletePost.execute(42)
        // then
        Truth.assertThat(result).isInstanceOf(Resource.Error::class.java)
        coCalledOnce { mockRepository.deletePost(42) }
    }
}