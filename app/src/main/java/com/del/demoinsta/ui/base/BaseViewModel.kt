package com.del.demoinsta.ui.base

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.del.demoinsta.R
import com.del.demoinsta.utils.common.Resource
import com.del.demoinsta.utils.network.NetworkHelper
import com.del.demoinsta.utils.rx.SchedulerProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.net.ssl.HttpsURLConnection

//
@SuppressLint("LogNotTimber")
abstract class BaseViewModel(

    //
    protected val schedulerProvider: SchedulerProvider,

    //
    protected val compositeDisposable: CompositeDisposable,

    //
    protected val networkHelper: NetworkHelper

) : ViewModel() {


    //
    private val TAG = BaseViewModel::class.simpleName + " : ";

    // for error : uses Resource.string.id (value)
    val messageStringId = MutableLiveData<Resource<Int>>()

    // networking error msg TODO : not cleared completely.
    val messageString = MutableLiveData<Resource<String>>()


    /**
     * return a string via LD
     */
    protected fun checkInternetConnectionWithMessage(): Boolean {
        Log.d(TAG, "checkInternetConnectionWithMessage: () ")

        //
        return if (networkHelper.isNetworkConnected()) {
            true
        } else {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
            false
        }
    }

    /**
     *
     */
    protected fun checkInternetConnection(): Boolean {
        Log.d(TAG, "checkInternetConnection: () ")
        return networkHelper.isNetworkConnected()
    }

    /**
     *
     */
    protected fun handleNetworkError(throwable: Throwable?) {
        Log.d(TAG, "handleNetworkError: () ")


        //
        throwable?.let {
            //
            networkHelper.castToNetworkError(it).run {
                //
                Log.i(TAG, "handleNetworkError: msg () " + it.message)

                // lol, kotlin too much concise
                // val status : NE = castToNetworkError(..)
                when (status) {


                    //
                    -1 -> messageStringId.postValue(Resource.error(R.string.network_default_error))

                    //
                    0 -> messageStringId.postValue(Resource.error(R.string.server_connection_error))

                    //
                    HttpsURLConnection.HTTP_UNAUTHORIZED -> {
                        forcedLogoutUser()
                        messageStringId.postValue(Resource.error(R.string.server_connection_error))
                    }

                    //
                    HttpsURLConnection.HTTP_INTERNAL_ERROR ->
                        messageStringId.postValue(Resource.error(R.string.network_internal_error))
                    //
                    HttpsURLConnection.HTTP_UNAVAILABLE ->
                        messageStringId.postValue(Resource.error(R.string.network_server_not_available))

                    //
                    else -> messageString.postValue(Resource.error(message))
                }
            }
        }
    }

    protected open fun forcedLogoutUser() {
        Log.d(TAG, "forcedLogoutUser: () ")
        // do something
    }

    //
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: () ")
        //
        compositeDisposable.clear()
    }


    /**
     * notify the VM that everything is set up, now if you want fetch something, you can go ahead.
     *
     * Again, this method seems optional, and just for the sake of repetition, we are doing this.
     */
    abstract fun notifyOnCreateFinished()
}