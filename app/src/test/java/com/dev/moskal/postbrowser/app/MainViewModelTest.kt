package com.dev.moskal.postbrowser.app

import com.dev.moskal.postbrowser.BaseTest
import com.dev.moskal.postbrowser.InstantExecutorExtension
import com.dev.moskal.postbrowser.coCalledOnce
import com.dev.moskal.postbrowser.domain.model.POST_NOT_SELECTED_ID
import com.dev.moskal.postbrowser.domain.model.Post
import com.dev.moskal.postbrowser.domain.model.Resource
import com.dev.moskal.postbrowser.domain.usecase.GetPost
import com.dev.moskal.postbrowser.domain.usecase.InitialFetch
import com.dev.moskal.postbrowser.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(InstantExecutorExtension::class)
internal class MainViewModelTest : BaseTest() {

    @RelaxedMockK
    lateinit var initialFetch: InitialFetch

    @MockK
    lateinit var getPost: GetPost

    lateinit var viewModel: MainViewModel

    @BeforeEach
    fun init() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = MainViewModel(initialFetch, getPost)
        every { getPost.execute(any()) } returns flowOf(mockk())
    }

    @Test
    fun `when viewModel is created then start initial fetch`() {
        // given
        val initialFetch = mockk<InitialFetch>(relaxed = true)
        // when
        MainViewModel(initialFetch, getPost)
        // then
        coCalledOnce { initialFetch.execute() }
    }

    @Test
    fun `when post is selected then propagate value`() {
        // given
        val postId = 42
        // when
        viewModel.selectPost(postId)
        // then
        assertThat(viewModel.selectedPost.getOrAwaitValue()).isEqualTo(postId)
    }

    @Test
    fun `when post is selected and delete then propagate unselected value`() {
        // given
        val flow = MutableStateFlow<Resource<Post?>>(Resource.Error(mockk()))
        every { getPost.execute(any()) } returns flow
        val postId = 42
        // when
        viewModel.selectPost(postId)
        flow.value = Resource.Success(null)

        // then
        assertThat(viewModel.selectedPost.getOrAwaitValue()).isEqualTo(POST_NOT_SELECTED_ID)
    }

    @Test
    fun `when post is unselected then propagate value`() {
        // given
        val postId = 42
        // when
        viewModel.selectPost(postId)
        viewModel.unselectPost()
        // then
        assertThat(viewModel.selectedPost.getOrAwaitValue()).isEqualTo(POST_NOT_SELECTED_ID)
    }
}