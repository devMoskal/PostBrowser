package com.dev.moskal.postbrowser.domain.model

/**
 * A generic class that contains data and status about loading this data.
 * Additional methods reduce boilerplate when using with data binding
 *
 */
sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val error: Throwable) : Resource<T>()

    fun isError() = this is Error

    fun isSuccess() = this is Success
}