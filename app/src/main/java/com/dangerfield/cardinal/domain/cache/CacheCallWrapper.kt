package com.dangerfield.cardinal.domain.cache

import kotlinx.coroutines.CoroutineDispatcher

/*
Cache call wrappers should be implemented by data layer to wrap cache calls made and
handle mapping to a data state of either the Domain Model OR a Cache Error depending on the result
of the cache call
 */
interface CacheCallWrapper {
    suspend fun<T> safeCacheCall(
        dispatcher: CoroutineDispatcher,
        cacheCall: suspend () -> T?
    ) : CacheResponse<T?>
}