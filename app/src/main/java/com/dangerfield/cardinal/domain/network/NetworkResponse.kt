package com.dangerfield.cardinal.domain.network

sealed class NetworkResponse<T>(
    val networkData: T? = null,
    val networkError: NetworkError? = null
) {
    class Success<T>(val data: T) : NetworkResponse<T>(data)
    class Error<T>(val error: NetworkError) : NetworkResponse<T>(null, error)
}