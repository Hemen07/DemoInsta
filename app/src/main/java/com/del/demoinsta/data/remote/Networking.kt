package com.del.demoinsta.data.remote


import android.annotation.SuppressLint
import android.util.Log
import com.del.demoinsta.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 *
 * create and configure  Retrofit Service
 */
@SuppressLint("LogNotTimber")
object Networking {


    private val TAG = Networking::class.simpleName + " : ";


    // 3 headers
    const val HEADER_API_KEY = "x-api-key"
    const val HEADER_ACCESS_TOKEN = "x-access-token"
    const val HEADER_USER_ID = "x-user-id"

    //timeout 60s
    private const val NETWORK_CALL_TIMEOUT = 60 * 1000L

    // this is header api  key value.
    lateinit var HEADER_API_KEY_VALUE: String

    /**
     * create and configure
     */
    fun create(
        headerApiKeyValue: String,
        baseUrl: String,
        cacheDir: File,
        cacheSize: Long
    ): NetworkService {

        Log.d(TAG, "create: () ")

        //
        HEADER_API_KEY_VALUE = headerApiKeyValue

        //
        return Retrofit
            .Builder()
            .baseUrl(baseUrl)

            //
            .client(getOkHttpClient(cacheDir, cacheSize))

            //
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(NetworkService::class.java)

    }

    // With spice ..........
    private fun getOkHttpClient(cacheDir: File, cacheSize: Long): OkHttpClient {
        Log.d(TAG, "getOkHttpClient: () ")
        return OkHttpClient
            .Builder()
            //
            .cache(Cache(cacheDir, cacheSize))
            //
            .addInterceptor(HttpLoggingInterceptor()
                // configuring Interceptor Logs for only in DEBUG
                .apply {

                    // if debug, log body and response.
                    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
                })
            .readTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }


}