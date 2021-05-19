package com.dangerfield.cardinal.data.util

import com.dangerfield.cardinal.domain.cache.CacheCallWrapper
import com.dangerfield.cardinal.domain.cache.CacheError
import com.dangerfield.cardinal.domain.cache.CacheResponse
import com.dangerfield.cardinal.domain.util.Constants.CACHE_TIMEOUT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class CacheCallWrapperImpl @Inject constructor(): CacheCallWrapper {

    override suspend fun <T> safeCacheCall(
        dispatcher: CoroutineDispatcher,
        cacheCall: suspend () -> T?
    ): CacheResponse<T?> {
        return withContext(dispatcher) {
            try {
                // throws TimeoutCancellationException
                withTimeout(CACHE_TIMEOUT) {
                    val result: CacheResponse<T?> =
                        CacheResponse.Success(
                            cacheCall.invoke()
                        )
                    result
                }
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                val error = when (throwable) {

                    is TimeoutCancellationException -> {
                        CacheError.Timeout()
                    }
                    else -> {
                        CacheError.Unknown()
                    }
                }
                val result: CacheResponse<T?> =
                    CacheResponse.Error(
                        error = error
                    )
                result
            }
        }
    }
}