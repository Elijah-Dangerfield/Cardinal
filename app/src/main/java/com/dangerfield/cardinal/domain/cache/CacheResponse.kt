package com.dangerfield.cardinal.domain.cache

sealed class CacheResponse<T>(
    val cacheData: T? = null,
    val cacheError: CacheError? = null
) {
    class Success<T>(val data: T) : CacheResponse<T>(data)
    class Error<T>(val error: CacheError, val data: T? = null) : CacheResponse<T>(data, error)
}