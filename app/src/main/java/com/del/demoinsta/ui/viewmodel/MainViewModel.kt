package com.del.demoinsta.ui.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.del.demoinsta.ui.base.BaseViewModel
import com.del.demoinsta.utils.common.Event
import com.del.demoinsta.utils.network.NetworkHelper
import com.del.demoinsta.utils.rx.SchedulerProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 *
 */
@SuppressLint("LogNotTimber")
class MainViewModel(
    //
    schedulerProvider: SchedulerProvider,
    //
    compositeDisposable: CompositeDisposable,
    //
    networkHelper: NetworkHelper,

    ) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    //
    private val TAG = MainViewModel::class.simpleName + " : ";


    //
    val homeNavigation = MutableLiveData<Event<Boolean>>()

    //
    val profileNavigation = MutableLiveData<Event<Boolean>>()

    //
    val photoNavigation = MutableLiveData<Event<Boolean>>()


    //
    init {
        Log.i(TAG, "init()")
    }

    override fun notifyOnCreateFinished() {
        // do something
        Log.d(TAG, "notifyOnCreateFinished: () ")

        //
        homeNavigation.postValue(Event(true))

    }


    //
    fun onHomeSelected() {
        Log.d(TAG, "onHomeSelected: () ")
        homeNavigation.postValue(Event(true))
    }

    fun onProfileSelected() {
        Log.d(TAG, "onProfileSelected: () ")
        profileNavigation.postValue(Event(true))
    }

    fun onPhotoSelected() {
        Log.d(TAG, "onPhotoSelected: () ")
        photoNavigation.postValue(Event(true))
    }

    // don't worry not calling onClear, BVM handling that
    // this is just specific to this class.
    fun clear() {
        Log.d(TAG, "clear: () ")
        // not needed
        //  compositeDisposable.clear()
    }
}