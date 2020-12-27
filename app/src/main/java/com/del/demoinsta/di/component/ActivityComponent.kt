package com.del.demoinsta.di.component

import com.del.demoinsta.di.ActivityScope
import com.del.demoinsta.di.module.ActivityModule
import com.del.demoinsta.ui.activity.LoginActivity
import com.del.demoinsta.ui.activity.MainActivity
import com.del.demoinsta.ui.activity.SplashActivity
import dagger.Component

/**
 *
 *  * EXPOSING getter method for dependencies that are inside in its respective module [ActivityModule]

 */
@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {

    /**
     *
     */
    fun inject(activity: SplashActivity)


    /**
     *
     */
    fun inject(activity: LoginActivity)

    /**
     *
     */
    fun inject(activity: MainActivity)

    // TODO : Should not use this as BaseActivity. Only Target the Child
}
