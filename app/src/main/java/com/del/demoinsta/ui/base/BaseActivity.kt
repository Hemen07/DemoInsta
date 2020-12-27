package com.del.demoinsta.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.del.demoinsta.InstagramApplication
import com.del.demoinsta.di.component.ActivityComponent
import com.del.demoinsta.di.component.DaggerActivityComponent
import com.del.demoinsta.di.module.ActivityModule
import javax.inject.Inject


/**
 *
 *
 */

@SuppressLint("LogNotTimber")
abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    //
    private val TAG = BaseActivity::class.simpleName + " : ";

    // All child class will use this.
    @Inject
    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: () ")


        //
        injectDependencies(buildActivityComponent())

        //
        setContentView(provideLayoutId())

        //
        setupView(savedInstanceState)

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
        Log.d(TAG, "setupObservers: () ")

        //
        viewModel.messageStringId.observe(this, {
            Log.i(TAG, "||||| setupObservers(): |||||||| messageStringId $it")

            // checking null
            it.data?.run {

                //this = it.data
                showMessage(this)
            }
//            showMessage(it.data)
        })

        //
        viewModel.messageString.observe(this, {
            Log.i(TAG, "||||| setupObservers(): |||||||| messageString $it")

            // checking null
            it.data?.run {
                showMessage(this)
            }
        })

    }

    // for child classes.....
    open fun goBack() {
        Log.d(TAG, "goBack: () ")
        onBackPressed()
    }

    /**
     *  build the ActivityComponent
     */
    private fun buildActivityComponent(): ActivityComponent {
        Log.d(TAG, "buildActivityComponent: () ")
        //
        return DaggerActivityComponent
            .builder()
            .applicationComponent((application as InstagramApplication).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()
    }


    /**
     * Helper method.
     */
    //
    private fun showMessage(message: String) =
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    //
    private fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))


    //
    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed: () ")
        //

        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStackImmediate()
        else super.onBackPressed()
    }

    /**
     * All child class must : No default/common implementation for these
     */
    // provide a layout for the class implementing this.
    @LayoutRes
    protected abstract fun provideLayoutId(): Int

    //  ActivityComponent is already built in BA
    // So, child class just need inject himself with activityComponent
    // EG : MA -> activityComponent.inject(this)
    protected abstract fun injectDependencies(activityComponent: ActivityComponent)

    // For setting up its child class xml views.
    protected abstract fun setupView(savedInstanceState: Bundle?)
}