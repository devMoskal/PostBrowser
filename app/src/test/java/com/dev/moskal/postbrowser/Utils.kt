package com.dev.moskal.postbrowser

import com.dev.moskal.postbrowser.domain.model.Resource
import com.google.common.truth.Subject
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

internal fun Subject.isResourceError() {
    isInstanceOf(Resource.Error::class.java)
}

internal fun Subject.isResourceSuccess() {
    isInstanceOf(Resource.Success::class.java)
}