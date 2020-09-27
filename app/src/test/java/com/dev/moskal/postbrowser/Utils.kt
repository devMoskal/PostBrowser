package com.dev.moskal.postbrowser

import io.mockk.MockKVerificationScope
import io.mockk.coVerify
import io.mockk.verify

internal fun calledOnce(verifyBlock: MockKVerificationScope.() -> Unit) =
    verify(exactly = 1, verifyBlock = verifyBlock)

internal fun coCalledOnce(verifyBlock: suspend MockKVerificationScope.() -> Unit) =
    coVerify(exactly = 1, verifyBlock = verifyBlock)

internal fun wasNotCalled(verifyBlock: MockKVerificationScope.() -> Unit) =
    verify(exactly = 0, verifyBlock = verifyBlock)

internal fun coWasNotCalled(verifyBlock: suspend MockKVerificationScope.() -> Unit) =
    coVerify(exactly = 0, verifyBlock = verifyBlock)