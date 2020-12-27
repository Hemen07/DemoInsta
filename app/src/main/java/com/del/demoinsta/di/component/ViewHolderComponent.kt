package com.del.demoinsta.di.component

import com.del.demoinsta.di.ViewHolderScope
import com.del.demoinsta.di.module.ViewHolderModule
import com.del.demoinsta.ui.itemviewholder.PostItemViewHolder
import dagger.Component

/**
 * EXPOSING getter method for dependencies that are inside in its respective module [ViewHolderModule]

 */


@ViewHolderScope
@Component(dependencies = [ApplicationComponent::class], modules = [ViewHolderModule::class])
interface ViewHolderComponent {


    //
    fun inject(viewHolder: PostItemViewHolder)
}
