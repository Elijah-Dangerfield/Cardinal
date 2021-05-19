package com.dangerfield.cardinal.data.util

import android.os.SystemClock
import androidx.collection.ArrayMap
import com.dangerfield.cardinal.domain.util.RateLimiter
import java.util.concurrent.TimeUnit

/**
 * Utility class that decides whether we should fetch some data or not.
 */
class RateLimiterImpl(timeout: Int, timeUnit: TimeUnit) : RateLimiter {
    private val timestamps = ArrayMap<String, Long>()
    private val timeout = timeUnit.toMillis(timeout.toLong())

    @Synchronized
    override fun shouldFetch(key: String): Boolean {
        val lastFetched = timestamps[key]
        val now = now()
        if (lastFetched == null) {
            timestamps[key] = now
            return true
        }
        if (now - lastFetched > timeout) {
            timestamps[key] = now
            return true
        }
        return false
    }

    private fun now() = SystemClock.uptimeMillis()

    @Synchronized
    override fun reset(key: String) {
        timestamps.remove(key)
    }
}