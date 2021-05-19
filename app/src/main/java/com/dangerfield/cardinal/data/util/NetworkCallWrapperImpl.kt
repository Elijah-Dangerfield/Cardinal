package com.dangerfield.cardinal.data.util


import com.dangerfield.cardinal.domain.network.NetworkCallWrapper
import com.dangerfield.cardinal.domain.network.NetworkError
import com.dangerfield.cardinal.domain.network.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/*
performs suspending network function under specified timeout and specified dispatcher
returns NetworkResponse dictating status of failure and success
 */
class NetworkCallWrapperImpl @Inject constructor(): NetworkCallWrapper {

    override suspend fun <T> safeNetworkCall(
        dispatcher: CoroutineDispatcher,
        timeout: Long,
        networkCall: suspend () -> T
    ): NetworkResponse<T> {
        return withContext(dispatcher) {
            try {
                // throws TimeoutCancellationException
                withTimeout(timeout) {
                    val result: NetworkResponse<T> =
                        NetworkResponse.Success(
                            networkCall.invoke()
                        )
                    result
                }
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                val error = when (throwable) {
                    is TimeoutCancellationException -> {
                        NetworkError.Timeout()
                    }
                    is IOException -> {
                        NetworkError.IO()
                    }
                    is HttpException -> {
                        NetworkError.Http(throwable.localizedMessage)
                    }
                    else -> {
                        NetworkError.Unknown()
                    }
                }
                val result: NetworkResponse<T> =
                    NetworkResponse.Error(
                        error = error
                    )
                result
            }
        }
    }
}