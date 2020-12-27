package com.del.demoinsta.di.component

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.del.demoinsta.InstagramApplication
import com.del.demoinsta.data.local.db.DatabaseService
import com.del.demoinsta.data.remote.NetworkService
import com.del.demoinsta.data.repository.UserRepository
import com.del.demoinsta.di.ApplicationContextQualifier
import com.del.demoinsta.di.TempDirectoryQualifier
import com.del.demoinsta.di.module.ApplicationModule
import com.del.demoinsta.utils.network.NetworkHelper
import com.del.demoinsta.utils.rx.SchedulerProvider
import dagger.Component
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.io.File
import javax.inject.Singleton


/**
 * EXPOSING getter method for dependencies that are inside in its respective module [ApplicationModule]
 *
 *
 */
@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    /**
     *
     */
    fun inject(app: InstagramApplication)

    /**
     *
     */
    fun getApplication(): Application

    /**
     *
     */
    @ApplicationContextQualifier
    fun getContext(): Context

    //
    // todo : Required only when this component is being used to get NW/DB/NH instances in other component such as Activity/Fragment.
    // todo : instance sharing  |||||||||||||||||||| -_-


    fun getNetworkService(): NetworkService

    fun getDatabaseService(): DatabaseService

    fun getSharedPreferences(): SharedPreferences

    fun getNetworkHelper(): NetworkHelper


    /**
     *
     * RX
     */

    fun getSchedulerProvider(): SchedulerProvider

    fun getCompositeDisposable(): CompositeDisposable


    // ctor injected, writing here, since other components can also use.
    // if not needed for other component, then no need to write.
    fun getUserRepository(): UserRepository


    // ctor injected and not needed to share.
//    fun getPostRepository(): PostRepository
    // same with PostCreateRepository


    // File
    // for temp directory -> image storing.
    @TempDirectoryQualifier
    fun getTempDirectory(): File

}