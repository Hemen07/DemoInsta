package com.del.demoinsta.di.component

import com.del.demoinsta.di.FragmentScope
import com.del.demoinsta.di.module.FragmentModule
import com.del.demoinsta.ui.fragment.HomeFragment
import com.del.demoinsta.ui.fragment.PhotoFragment
import com.del.demoinsta.ui.fragment.ProfileFragment
import dagger.Component

/**
 * EXPOSING getter method for dependencies that are inside in its respective module [FragmentModule]
 *
 */

@FragmentScope
@Component(dependencies = [ApplicationComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {

    //
    fun inject(fragment: HomeFragment)

    //
    fun inject(fragment: PhotoFragment)

    //
    fun inject(fragment: ProfileFragment)


}