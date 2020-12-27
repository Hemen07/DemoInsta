package com.del.demoinsta.ui.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import com.del.demoinsta.ui.base.BaseViewModel
import com.del.demoinsta.utils.network.NetworkHelper
import com.del.demoinsta.utils.rx.SchedulerProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable


/**
 *
 *
 */
@SuppressLint("LogNotTimber")
class ProfileViewModel(
    //
    schedulerProvider: SchedulerProvider,
    //
    compositeDisposable: CompositeDisposable,
    //
    networkHelper: NetworkHelper
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    //
    private val TAG = ProfileViewModel::class.simpleName + " : ";

    //
    init {
        Log.i(TAG, "init()");
    }


    // you know, why I use this lol.
    override fun notifyOnCreateFinished() {
        Log.d(TAG, "notifyOnCreateFinished: () ")

    }


    // don't worry not calling onClear, BVM handling that
    // this is just specific to this class.
    fun clear() {
        Log.d(TAG, "clear: () ")
        // not needed
        //  compositeDisposable.clear()
    }
}