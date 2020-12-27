package com.del.demoinsta.di.module

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.del.demoinsta.InstagramApplication
import com.del.demoinsta.data.local.db.DatabaseService
import com.del.demoinsta.data.remote.Endpoints
import com.del.demoinsta.data.remote.NetworkService
import com.del.demoinsta.data.remote.Networking
import com.del.demoinsta.di.ApplicationContextQualifier
import com.del.demoinsta.di.TempDirectoryQualifier
import com.del.demoinsta.utils.common.FileUtils
import com.del.demoinsta.utils.network.NetworkHelper
import com.del.demoinsta.utils.network.NetworkHelperImp
import com.del.demoinsta.utils.rx.RxSchedulerProvider
import com.del.demoinsta.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Singleton


/**
 * |||||||||| Application ||||||||||
 */
@SuppressLint("LogNotTimber")
@Module
class ApplicationModule(private val application: InstagramApplication) {

    //
    private val TAG = ApplicationModule::class.simpleName + " : ";

    //
    init {
        Log.i(TAG, "init: () ")
    }

    /**
     *
     */
    @Singleton
    @Provides
    fun provideApplication(): Application = application

    /**
     *
     */
    @ApplicationContextQualifier
    @Singleton
    @Provides
    fun provideContext(): Context = application

    /**
     * Since this function do not have @Singleton then each time CompositeDisposable is injected
     * then a new instance of CompositeDisposable will be provided
     */
    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    /**
     *
     */
    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = RxSchedulerProvider()

    /**
     *
     */
    @Singleton
    @Provides
    fun provideSharedPreferences(): SharedPreferences =
        application.getSharedPreferences("bootcamp-instagram-project-prefs", Context.MODE_PRIVATE)


    /**
     * Creating directory for image storing for internal storage.
     *
     * Added a qualifier, so in future if we write more provideOtherDirectory()
     * It will be helpful to distinguish.
     */
    @TempDirectoryQualifier
    @Singleton
    @Provides
    fun provideTempDirectory() = FileUtils.getDirectory(application, "temp")

    /**
     * || DB ||
     * We need to write @Singleton on the provide method if we are create the instance inside this method
     * to make it singleton. Even if we have written @Singleton on the instance's class
     */
    @Provides
    @Singleton
    fun provideDatabaseService(): DatabaseService {
        Log.d(TAG, "provideDatabaseService: () ")
        //
        return Room.databaseBuilder(
            application, DatabaseService::class.java,
            "bootcamp-instagram-project-db"
        ).build()

    }


    /**
     * || NS ||
     */
    @Singleton // scope
    @Provides
    fun provideNetworkService(): NetworkService = Networking.create(

        Endpoints.HEADER_API_KEY_VALUE,
        Endpoints.BASE_URL,
        application.cacheDir,
        10 * 1024 * 1024  // 10 MB
    )


    /**
     * || NH ||
     */
    @Singleton
    @Provides
    fun provideNetworkHelper(): NetworkHelper {
        Log.d(TAG, "provideNetworkHelper: () ")
        //
        return NetworkHelperImp(application)
    }
}