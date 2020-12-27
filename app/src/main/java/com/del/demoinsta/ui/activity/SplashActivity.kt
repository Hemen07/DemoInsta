package com.del.demoinsta.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.del.demoinsta.R
import com.del.demoinsta.di.component.ActivityComponent
import com.del.demoinsta.ui.base.BaseActivity
import com.del.demoinsta.ui.viewmodel.SplashViewModel
import com.del.demoinsta.utils.common.Event


/**
 *
 *
 */
@SuppressLint("LogNotTimber")
class SplashActivity : BaseActivity<SplashViewModel>() {

    companion object {
        const val TAG = "SplashActivity"
    }

    // What's happening in BA
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.e(TAG, "onCreate: () ")
//        injectDependencies(buildActivityComponent())
//        setContentView(provideLayoutId())
//        setupView(savedInstanceState)
//        setupObservers()
//        viewModel.notifyOnCreateFinished()
//    }

    override fun provideLayoutId(): Int {
        Log.d(TAG, "provideLayoutId: () ")
        return R.layout.activity_splash
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        Log.d(TAG, "injectDependencies: () ")
        activityComponent.inject(this)
    }


    override fun setupView(savedInstanceState: Bundle?) {
        Log.d(TAG, "setupView: () ")

        //


    }

    //
    override fun setupObservers() {
        Log.d(TAG, "setupObservers: () ")


        // Observer for Main launch
        // Event is used by the view model to tell the activity to launch another activity
        // view model also provided the Bundle in the event that is needed for the Activity
        viewModel.launchMain.observe(this, Observer<Event<Map<String, String>>> {
            it.getIfNotHandled()?.run {
                Log.i(TAG, "setupObservers: () ||||||||||    |||||||||||  launchMain ")
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        })


        // Observer for Login launch
        viewModel.launchLogin.observe(this, Observer<Event<Map<String, String>>> {
            it.getIfNotHandled()?.run {
                Log.i(TAG, "setupObservers: () ||||||||||    |||||||||||  launchLogin ")
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        })
    }
}