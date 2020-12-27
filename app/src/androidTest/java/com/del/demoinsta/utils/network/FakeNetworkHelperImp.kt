package com.del.demoinsta.utils.network

import android.content.Context

/**
 * Fake
 */
class FakeNetworkHelperImp (private val context: Context) : NetworkHelper {

    override fun isNetworkConnected(): Boolean {
        return true
    }

    override fun castToNetworkError(throwable: Throwable): NetworkError {
        return NetworkHelperImp(context).castToNetworkError(throwable)
    }
}