package com.dev.moskal.postbrowser

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> Flow<T>.test(scope: CoroutineScope): TestFlowObserver<T> {
    return TestFlowObserver(scope, this)
}

class TestFlowObserver<T>(
    scope: CoroutineScope,
    flow: Flow<T>
) {
    private val values = mutableListOf<T>()
    private val job: Job = scope.launch {
        flow.collect { values.add(it) }
    }

    fun assertNoValues(): TestFlowObserver<T> {
        assertThat(this.values).isEmpty()
        return this
    }

    fun assertValues(vararg values: T): TestFlowObserver<T> {
        assertThat(values.toList()).isEqualTo(this.values)
        return this
    }

    fun assertHasSize(size: Int): TestFlowObserver<T> {
        assertThat(size).isEqualTo(this.values.size)
        return this
    }

    fun finish() {
        job.cancel()
    }
}