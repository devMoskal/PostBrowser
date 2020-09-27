package com.dev.moskal.postbrowser.domain.model

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
