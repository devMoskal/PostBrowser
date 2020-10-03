package com.dev.moskal.postbrowser.domain.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

/**
 * Returns a flow containing only values of the original flow that matches the given [predicate].
 * In case of filtering out item
 */
inline fun <T> Flow<T>.filterWithSideEffect(
    crossinline predicate: suspend (T) -> Boolean,
    crossinline actionOnFilterOut: suspend (T) -> Unit
): Flow<T> =
    transform { value ->
        if (predicate(value)) return@transform emit(value) else actionOnFilterOut.invoke(value)
    }