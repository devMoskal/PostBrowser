package com.dev.moskal.postbrowser.domain.model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * A generic class that contains data and status about loading this data.
 * Additional methods reduce boilerplate when using with data binding
 *
 */
sealed class Resource<T>(val data: T? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    data class Error<T>(val error: Throwable) : Resource<T>()

    fun isSuccess() = this is Success
    fun isError() = this is Error
}

suspend fun <T : Any> asResource(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> T
): Resource<T> = withContext(dispatcher) {
    try {
        block().data
    } catch (e: Exception) {
        Resource.Error(e)
    }
}

fun <T> Flow<T>.asResourceFlow(): Flow<Resource<T>> = this
    .map { Resource.Success(it) as Resource<T> }
    .catch {
        emit(Resource.Error(it))
    }

private val <T : Any> T.data get() = Resource.Success(this)