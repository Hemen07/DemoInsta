package com.del.demoinsta.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.del.demoinsta.InstagramApplication
import com.del.demoinsta.di.component.DaggerFragmentComponent
import com.del.demoinsta.di.component.FragmentComponent
import com.del.demoinsta.di.module.FragmentModule
import javax.inject.Inject


/**
 *
 * Boundary in generic.
 *
 * Not using onCreate, instead using onViewCreated.
 *
 * Like BA, we are also observing common LD of BVM
 */
@SuppressLint("LogNotTimber")
abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    //
    protected var TAG = BaseFragment::class.simpleName + " : ";

    // All child class will use this.
    @Inject
    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: () ")
    }

    // I will not use onCreate.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG, "onCreateView: ()  BASE")
        return inflater.inflate(provideLayoutId(), container, false)
    }


    //
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e(TAG, "onViewCreated: () BASE ")
        super.onViewCreated(view, savedInstanceState)

        //
        injectDependencies(buildFragmentComponent())

        //
        setupView(view)

        //
        setupObservers()

        // notify VM, that I have done setting up, so if you want to fetch anything, then go ahead.
        viewModel.notifyOnCreateFinished()

    }


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
        Log.d(TAG, "setupObservers: () : BASE ")

        //
        viewModel.messageStringId.observe(viewLifecycleOwner, {
            Log.i(TAG, "||||| setupObservers(): ||||||||  BASE : messageStringId $it")
            // checking null
            it.data?.run {

                //this = it.data
                showMessage(this)
            }
        })

        //
        viewModel.messageString.observe(viewLifecycleOwner, {
            Log.i(TAG, "||||| setupObservers(): |||||||| BASE : messageString $it")
            // checking null
            it.data?.run {

                //this = it.data
                showMessage(this)
            }
        })

    }


    // simone : go back.
    fun goBack() {
        Log.d(TAG, "goBack: () BASE ")
        if (activity is BaseActivity<*>) (activity as BaseActivity<*>).goBack()
    }


    /**
     *  build the FragmentComponent
     */
    private fun buildFragmentComponent(): FragmentComponent {
        Log.d(TAG, "buildFragmentComponent: () BASE")
        //
        return DaggerFragmentComponent
            .builder()
            .applicationComponent((context!!.applicationContext as InstagramApplication).applicationComponent)
            .fragmentModule(FragmentModule(this))
            .build()
    }


    /**
     * Helper method.
     */
    //
    private fun showMessage(message: String) =
        Toast.makeText(InstagramApplication.getInstance(), message, Toast.LENGTH_SHORT).show()

    //
    protected fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))


    /**
     * All child class must : No default/common implementation for these
     */
    // provide a layout for the class implementing this.
    @LayoutRes
    protected abstract fun provideLayoutId(): Int

    //  FragmentComponent is already built in BF
    // So, child class just need inject himself with fragmentComponent
    // EG : HF -> fragmentComponent.inject(this)
    protected abstract fun injectDependencies(fragmentComponent: FragmentComponent)

    // For setting up its child class xml views.
    protected abstract fun setupView(view: View)
}