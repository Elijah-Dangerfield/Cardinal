package com.dangerfield.cardinal.domain.network

import com.dangerfield.cardinal.domain.util.Constants
import kotlinx.coroutines.CoroutineDispatcher

/*
Network call wrappers should be implemented by data layer to wrap networks calls made and
handle mapping to a data state of either the Domain Model OR a Network Error depending on the result
of the network call
 */
interface NetworkCallWrapper {
    suspend fun<T> safeNetworkCall(
        dispatcher: CoroutineDispatcher,
        timeout: Long = Constants.NETWORK_TIMEOUT,
        networkCall: suspend () -> T,
    ) : NetworkResponse<T>
}