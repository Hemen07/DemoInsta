package com.del.demoinsta.ui.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.del.demoinsta.data.model.Post
import com.del.demoinsta.ui.base.BaseViewModel
import com.del.demoinsta.utils.common.Event
import com.del.demoinsta.utils.network.NetworkHelper
import com.del.demoinsta.utils.rx.SchedulerProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Communication
 * This is for sharing VM of main for fragments
 *
 * HF and Add Photo F
 *
 *
 *
 * Adding  post to Add Photo F will lead to HF and item will be shown at the top pos of RV.
 *
 *
 */
@SuppressLint("LogNotTimber")
class MainSharedViewModel(
    //
    schedulerProvider: SchedulerProvider,
    //
    compositeDisposable: CompositeDisposable,
    //
    networkHelper: NetworkHelper
//
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    //
    private val TAG = MainSharedViewModel::class.simpleName + " : ";


    // will be observed by the container class : MainActivity
    val homeRedirection = MutableLiveData<Event<Boolean>>()

    /**
     * will be observed by Home Fragment
     *
     * So, when a new post gets created in [com.del.demoinsta.ui.fragment.PhotoFragment]
     * we will redirect to MA, to switch to HF
     * and display the newly added item at the top of RV
     */
    val newPost: MutableLiveData<Event<Post>> = MutableLiveData()

    /**
     * Just a method to tell homeRedirection LD to post sth.
     */
    fun onHomeRedirect() {
        Log.d(TAG, "onHomeRedirect: () ")
        homeRedirection.postValue(Event(true))
    }


    override fun notifyOnCreateFinished() {
        Log.d(TAG, "notifyOnCreateFinished: () ")
    }

}