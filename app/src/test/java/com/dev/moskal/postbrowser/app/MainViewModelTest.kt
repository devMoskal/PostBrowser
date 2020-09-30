package com.dev.moskal.postbrowser.app

import com.dev.moskal.postbrowser.BaseTest
import com.dev.moskal.postbrowser.coCalledOnce
import com.dev.moskal.postbrowser.domain.usecase.InitialFetch
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class MainViewModelTest : BaseTest() {

    @Test
    fun `when viewModel is created then start initial fetch`() {
        // given
        val initialFetch = mockk<InitialFetch>(relaxed = true)
        // when
        MainViewModel(initialFetch)
        // then
        coCalledOnce { initialFetch.execute() }
    }
}