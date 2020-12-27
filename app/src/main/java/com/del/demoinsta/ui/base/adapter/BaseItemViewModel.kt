package com.del.demoinsta.ui.base.adapter

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.del.demoinsta.ui.base.BaseViewModel
import com.del.demoinsta.utils.network.NetworkHelper
import com.del.demoinsta.utils.rx.SchedulerProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable


/**
 * - Base class for item/row views.
 * - accepts Any (Java equivalent Object)
 */
@SuppressLint("LogNotTimber")
abstract class BaseItemViewModel<T : Any>
    (

    //
    schedulerProvider: SchedulerProvider,
    //
    compositeDisposable: CompositeDisposable,
    //
    networkHelper: NetworkHelper

) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    //
    private val TAG = BaseItemViewModel::class.simpleName + " : ";

    private val LOG_DEBUG = false;


    //
    init {
        if (LOG_DEBUG) Log.i(TAG, "init()")
    }

    /**
     * This LD will be used for binding a <T> Data
     * T is one item of an list.
     * When RV onBindCalled -> BIVH -> Here -> go back to BIVH
     */
    val dataMutableLiveData = MutableLiveData<T>()


    /**
     * This is for bind Data  (onBindVIewHolder)
     *
     * When bind data arrives -
     *
     * from Adapter -> BIVH -> HERE
     *
     * HERE, via LD : sent back to BIVH, which is observing.
     *
     * uses [dataMutableLiveData] LD
     */
    fun bindData(data: T) {
        if (LOG_DEBUG) Log.d(TAG, "bindData: () ")
        this.dataMutableLiveData.postValue(data)
    }


    /**
     * BVM onCleared is triggered automatically by System for A/F.
     *
     * but we have ViewHolder, which is not LC owner
     *
     * so we need to call manually, since won't be triggered automatically coz of not owner of LC
     *
     * We will call it from ViewHolder class
     */
    fun manualOnCleared() {
        if (LOG_DEBUG) Log.d(TAG, "manualOnCleared: () ")
        // calling base's method manually.
        onCleared()
    }

}