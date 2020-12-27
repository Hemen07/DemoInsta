package com.del.demoinsta.utils.network

import android.annotation.SuppressLint


/**
 *
 */
@SuppressLint("LogNotTimber")
interface NetworkHelper {


    /**
     *
     */
    fun isNetworkConnected(): Boolean

    /**
     * Provide the throwable from Rx/Flow
     *
     * @param throwable : [Throwable], instance.
     * @return Instance of [NetworkError]
     */
    fun castToNetworkError(throwable: Throwable): NetworkError
}