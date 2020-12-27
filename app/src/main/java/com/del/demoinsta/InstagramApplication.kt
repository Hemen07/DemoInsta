package com.del.demoinsta

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.del.demoinsta.di.component.ApplicationComponent
import com.del.demoinsta.di.component.DaggerApplicationComponent
import com.del.demoinsta.di.module.ApplicationModule
import io.reactivex.rxjava3.plugins.RxJavaPlugins

/**
 *
 */
@SuppressLint("LogNotTimber")
class InstagramApplication : Application() {

    //
    companion object {

        //
        private val TAG = InstagramApplication::class.simpleName + " : ";

        //
        lateinit var sInstance: InstagramApplication
            // this is for that a value can’t be assigned from an external class.
            private set

        //
        fun getInstance(): InstagramApplication = sInstance
    }

    //
    lateinit var applicationComponent: ApplicationComponent

    //
    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate: () ")

        //
        sInstance = this;

        //
        injectDependencies()


        // set up for Undelivered Exception
        RxJavaPlugins.setErrorHandler { throwable ->
            Log.w(TAG, "Undelivered Exception received......-_-  " + throwable.message)
        }
    }


    /**
     *
     */
    private fun injectDependencies() {
        Log.d(TAG, "injectDependencies: () ")
        //
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }


    // Needed to replace the component with a test specific one
    fun setComponent(applicationComponent: ApplicationComponent) {
        this.applicationComponent = applicationComponent
    }
}