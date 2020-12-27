package com.del.demoinsta.ui.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.del.demoinsta.data.repository.UserRepository
import com.del.demoinsta.ui.base.BaseViewModel
import com.del.demoinsta.utils.common.Event
import com.del.demoinsta.utils.network.NetworkHelper
import com.del.demoinsta.utils.rx.SchedulerProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable


/**
 *
 */
@SuppressLint("LogNotTimber")
class SplashViewModel(
    //
    schedulerProvider: SchedulerProvider,
    //
    compositeDisposable: CompositeDisposable,
    //
    networkHelper: NetworkHelper,
    //
    val userRepository: UserRepository


) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    //
    private val TAG = SplashViewModel::class.simpleName + " : ";

    //
    init {
        Log.i(TAG, "init()")
    }

    // LD
    // Event is used by the view model to tell the activity to launch another Activity

    // for MA
    val launchMain: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    // for Login
    val launchLogin: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    override fun notifyOnCreateFinished() {
        Log.d(TAG, "notifyOnCreateFinished: () ")


        // Empty Bundle passed to Activity in Event that is needed by the other Activity
        // Here in actual application we will decide which screen to open based on
        // either the user is logged in or not

        // Empty map of key and serialized value is passed to Activity in Event that is needed by the other Activity

        if (userRepository.getCurrentUser() != null) {
            Log.i(TAG, "notifyOnCreateFinished: LAUNCH -> MAIN ")
            //
            launchMain.postValue(Event(emptyMap()))
        } else {
            Log.i(TAG, "notifyOnCreateFinished: LAUNCH -> LOGIN ")
            //
            launchLogin.postValue(Event(emptyMap()))
        }
    }
}