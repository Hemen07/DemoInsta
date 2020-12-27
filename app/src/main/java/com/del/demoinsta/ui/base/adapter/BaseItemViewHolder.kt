package com.del.demoinsta.ui.base.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.del.demoinsta.InstagramApplication
import com.del.demoinsta.di.component.DaggerViewHolderComponent
import com.del.demoinsta.di.component.ViewHolderComponent
import com.del.demoinsta.di.module.ViewHolderModule
import javax.inject.Inject

/**
 *
 * implements  CUSTOM LC Owner
 * - this way it can observe data LD of BIVM BaseItemVIewModel
 *
 * We also need Component for this BIVH.
 *
 * NOTE THAT :
 * LC aware component is RV Adapter.
 *
 * This is more of making CUSTOM Class LC Owner
 */
@SuppressLint("LogNotTimber")
abstract class BaseItemViewHolder<T : Any, VM : BaseItemViewModel<T>>
    (
    //
    @LayoutRes layoutId: Int,
    //
    parent: ViewGroup

//
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)),
    LifecycleOwner {

    //
    private val TAG = BaseItemViewHolder::class.simpleName + " : ";

    private val LOG_DEBUG = false;

    // BIVM , supply from outside
    @Inject
    lateinit var viewModel: VM

    // using this, to make a component LC aware.
    @Inject
    lateinit var lifecycleRegistry: LifecycleRegistry

    // current Data of holder item.
    protected var tData: T? = null

    //
    init {
        if (LOG_DEBUG) Log.i(TAG, "init()")
        // triggered when ctor is called.
        //
        onCreate()
    }


    override fun getLifecycle(): Lifecycle {
        if (LOG_DEBUG) Log.d(TAG, "getLifecycle: () ")
        //
        return lifecycleRegistry
    }


    /**
     *
     * BA -> onBindVH() -> to Here.
     *
     */
    open fun bind(data: T) {
        if (LOG_DEBUG) Log.d(TAG, "bind: () ")

        // just storing current item data in-case, obviously you can access via vm's ld
        this.tData = data
        //
        viewModel.bindData(data)
    }

    //
    protected fun onCreate() {
        if (LOG_DEBUG) Log.d(TAG, "onCreate: () ")


        // now, inject dependencies
        injectDependencies(buildViewHolderComponent())


        // do only after injection, else null
        // mark the state
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        lifecycleRegistry.currentState = Lifecycle.State.CREATED


        // itemView available coz ----- extending VH
        setupView(itemView)
        //
        setupObservers()

    }

    //
    public fun onStart() {
        if (LOG_DEBUG) Log.e(TAG, "onStart: () ")
        // mark the state
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    //
    public fun onStop() {
        if (LOG_DEBUG) Log.e(TAG, "onStop: () ")
        // mark the state
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    //
    public fun onDestroy() {
        if (LOG_DEBUG) Log.e(TAG, "onDestroy: () ")
        // mark the state
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }


    // ----------------------- almost same as other base class functionalities -----------
    /**
     * Common for all child
     *
     * Plus, If child class has other LD to observe, then override this and setup other LD.
     * If not, then no need to override this.
     *
     *
     * GOOD DOC.
     *
     * open - can be overridden, by default is final in kotlin.
     */
    protected open fun setupObservers() {
        if (LOG_DEBUG) Log.d(TAG, "setupObservers: () ")

        //
        viewModel.messageStringId.observe(this, {
            Log.i(TAG, "||||| setupObservers(): |||||||| messageStringId $it")

            // checking null
            it.data?.run {

                //this = it.data
                showMessage(this)
            }
        })

        //
        viewModel.messageString.observe(this, {
            Log.i(TAG, "||||| setupObservers(): |||||||| messageString $it")
            // checking null
            it.data?.run {

                //this = it.data
                showMessage(this)
            }
        })

    }

    /**
     *  build the ViewHolderComponent
     */
    private fun buildViewHolderComponent(): ViewHolderComponent {
        if (LOG_DEBUG) Log.d(TAG, "buildViewHolderComponent: () ")
        //
        return DaggerViewHolderComponent
            .builder()
            .applicationComponent((itemView.context.applicationContext as InstagramApplication).applicationComponent)
            .viewHolderModule(ViewHolderModule(this))
            .build()
    }


    /**
     * Helper method.
     */
    //
    protected fun showMessage(message: String) =
        Toast.makeText(itemView.context, message, Toast.LENGTH_SHORT).show()

    //
    private fun showMessage(@StringRes resId: Int) = showMessage(itemView.context.getString(resId))


    //  ActivityComponent is already built in BA
    // So, child class just need inject himself with activityComponent
    // EG : MA -> activityComponent.inject(this)
    protected abstract fun injectDependencies(viewHolderComponent: ViewHolderComponent)

    // For setting up its child class xml views.
    protected abstract fun setupView(view: View)
}