package com.del.demoinsta.di.module

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleRegistry
import com.del.demoinsta.di.ViewHolderScope
import com.del.demoinsta.ui.base.adapter.BaseItemViewHolder
import dagger.Module
import dagger.Provides

/**
 * |||||||||| ViewHolder ||||||||||
 */
@SuppressLint("LogNotTimber")
@Module
class ViewHolderModule(private val viewHolder: BaseItemViewHolder<*, *>) {

    private val TAG = ViewHolderModule::class.simpleName + " : ";

    //
    init {
     //   Log.i(TAG, "init()")
    }


    @ViewHolderScope  // S
    @Provides
    fun provideLifeCycleRegistry(): LifecycleRegistry {

        // class who implements LCOwner, can be passed, for A/F you don't need to implement
        return LifecycleRegistry(viewHolder)
    }

}