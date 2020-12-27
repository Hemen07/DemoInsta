package com.del.demoinsta.utils


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Singleton
import kotlin.reflect.KClass

/**
 *
 * ViewModelProviderFactory
 *
 * -
 *
 */
@Singleton
class ViewModelProviderFactory<T : ViewModel>(

    // KClass is the holder of class of type ViewModel that needs to be inject
    private val kClass: KClass<T>,


    // when creator lambda is called then that module creates and return the instance of ViewModel
    private val creator: () -> T


) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalArgumentException::class)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //
        if (modelClass.isAssignableFrom(kClass.java)) {

            //
            return creator() as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}

