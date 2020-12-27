package com.del.demoinsta.di.module

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.del.demoinsta.data.repository.UserRepository
import com.del.demoinsta.di.ActivityContextQualifier
import com.del.demoinsta.di.ActivityScope
import com.del.demoinsta.ui.base.BaseActivity
import com.del.demoinsta.ui.viewmodel.LoginViewModel
import com.del.demoinsta.ui.viewmodel.MainSharedViewModel
import com.del.demoinsta.ui.viewmodel.MainViewModel
import com.del.demoinsta.ui.viewmodel.SplashViewModel
import com.del.demoinsta.utils.ViewModelProviderFactory
import com.del.demoinsta.utils.network.NetworkHelper
import com.del.demoinsta.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * This is Responsible for creating required dependencies for consumer
 *  Component is the Management which takes request from Consumer and gives from Modules.
 * |||||||||| This module is for Activity
 *
 * we can pass any class that extends BaseActivity which take
 * BaseViewModel subclass as parameter
 *
 */

@SuppressLint("LogNotTimber")
@Suppress("MoveLambdaOutsideParentheses")
@Module
class ActivityModule(private val activity: BaseActivity<*>) {

    private val TAG = ActivityModule::class.simpleName + " : ";

    //
    init {
        Log.i(TAG, "init()")
    }


    @ActivityContextQualifier // Q
    @ActivityScope  // S
    @Provides
    fun provideContext(): Context = activity


    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(activity)


    // --------------------------------------- SA ---------------------------------


    @Provides
    fun provideSplashViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository

    ): SplashViewModel {
        Log.d(TAG, "provideSplashViewModel: () ")
        //
        return ViewModelProvider(

            //
            activity,
            //
            ViewModelProviderFactory<SplashViewModel>(
                // target class, which instance will be created by lambda.
                SplashViewModel::class,

                // we need to supply a lambda which will return MVM instance
                // creating instance of SVM, Again, this will be called by the System
                {

                    SplashViewModel(
                        schedulerProvider,
                        compositeDisposable,
                        networkHelper,
                        userRepository
                    )
                }
            )
        ).get(SplashViewModel::class.java)
    }


    // --------------------------------------- LA ---------------------------------


    @Provides
    fun provideLoginViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository

    ): LoginViewModel {
        Log.d(TAG, "provideLoginViewModel: () ")
        //
        return ViewModelProvider(activity, ViewModelProviderFactory(LoginViewModel::class) {
            LoginViewModel(schedulerProvider, compositeDisposable, networkHelper, userRepository)
        }).get(LoginViewModel::class.java)
    }


    // --------------------------------------- MA ---------------------------------
    @Provides
    fun provideMainViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper

    ): MainViewModel {
        Log.d(TAG, "provideMainViewModel: () ")
        //
        return ViewModelProvider(activity, ViewModelProviderFactory(MainViewModel::class) {
            MainViewModel(schedulerProvider, compositeDisposable, networkHelper)
        }).get(MainViewModel::class.java)
    }

    // Shared VM.

    /**
     * comm b/w Photo F and HF via MA's container
     *
     */
    @Provides
    fun provideMainSharedViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    ): MainSharedViewModel {
        Log.d(TAG, "provideMainSharedViewModel: () ")

        //
        return ViewModelProvider(activity, ViewModelProviderFactory(MainSharedViewModel::class) {
            MainSharedViewModel(schedulerProvider, compositeDisposable, networkHelper)
        }).get(MainSharedViewModel::class.java)

    }

}