package com.del.demoinsta.utils.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.del.demoinsta.utils.log.Logger
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import javax.inject.Singleton


/**
 * Concrete class
 */
@Singleton
@SuppressLint("LogNotTimber")
class NetworkHelperImp constructor(private val context: Context) : NetworkHelper {

    companion object {
        private const val TAG = "NetworkHelper"
    }

    /**
     *
     */
    override fun isNetworkConnected(): Boolean {
        Log.d(TAG, "isNetworkConnected: () ")

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }

    /**
     * Provide the throwable from Rx/Flow
     *
     * @param throwable : [Throwable], instance.
     * @return Instance of [NetworkError]
     */
    override fun castToNetworkError(throwable: Throwable): NetworkError {
        Log.d(TAG, "castToNetworkError: () ")

        //
        val defaultNetworkError = NetworkError()

        // based on type of throwable, we will return [NetworkError].
        try {


            // what is this ConnectException
            // typically SocketConnection refuse.
            if (throwable is ConnectException) return NetworkError(0, "0")

            // same as  !(obj is String), so, if throwable is not an instance of HttpException,
            // then use default,  default is -1,  -1
            if (throwable !is HttpException) {

                return defaultNetworkError
            }

            // else -> throwable is HttpException
            // can access the code.
            // create an instance  of NE from error Body. Body has all detail of failed response.
            // serialized to NE
            else {

                val networkError: NetworkError = GsonBuilder()
                    //
                    .excludeFieldsWithoutExposeAnnotation()
                    .create()
                    .fromJson(throwable.response()?.errorBody()?.string(), NetworkError::class.java)

                //
                return NetworkError(throwable.code(), networkError.statusCode, networkError.message)
            }


        } catch (e: IOException) {
            Logger.e(TAG, e.toString())
        } catch (e: JsonSyntaxException) {
            Logger.e(TAG, e.toString())
        } catch (e: NullPointerException) {
            Logger.e(TAG, e.toString())
        }

        // returning default as fail safe.
        return defaultNetworkError
    }
}