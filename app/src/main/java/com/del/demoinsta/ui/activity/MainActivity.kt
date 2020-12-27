package com.del.demoinsta.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.del.demoinsta.R
import com.del.demoinsta.di.component.ActivityComponent
import com.del.demoinsta.ui.base.BaseActivity
import com.del.demoinsta.ui.fragment.HomeFragment
import com.del.demoinsta.ui.fragment.PhotoFragment
import com.del.demoinsta.ui.fragment.ProfileFragment
import com.del.demoinsta.ui.viewmodel.MainSharedViewModel
import com.del.demoinsta.ui.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

/**
 *
 * Holds BottomNavigationView
 *
 * todo : add FragmentStatelistener
 *
 * todo : avoid multiple click RELOADING. // DONE
 *
 *
 * Note :
 *
 * used add/hide instead of replace.
 * replace triggers all LC.
 *
 */
@SuppressLint("LogNotTimber")
class MainActivity : BaseActivity<MainViewModel>() {

    //
    private val TAG = MainActivity::class.simpleName + " : ";


    private lateinit var bottomNavigation: BottomNavigationView

    //
    private var activeFragment: Fragment? = null


    // this VM for shared comm b/w fragment and parents.
    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    // What's happening in BA
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.e(TAG, "onCreate: () ")
//        injectDependencies(buildActivityComponent())
//        setContentView(provideLayoutId())
//        setupView(savedInstanceState)
//        setupObservers()
//        viewModel.notifyOnCreateFinished()
//    }


    override fun provideLayoutId(): Int {
        Log.d(TAG, "provideLayoutId: () ")
        return R.layout.activity_main
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        Log.d(TAG, "injectDependencies: () ")
        activityComponent.inject(this)
    }


    override fun setupView(savedInstanceState: Bundle?) {
        Log.d(TAG, "setupView: () ")

        //
        bottomNavigation = findViewById(R.id.bottomNavigation)

        //
        bottomNavigation.run {
            itemIconTintList = null
            setOnNavigationItemSelectedListener {

                when (it.itemId) {
                    R.id.itemHome -> {
                        Log.i(TAG, "bottomNavigation : HOME  ")

                        //
                        if (activeFragment !is HomeFragment)
                            viewModel.onHomeSelected()
                        //
                        true
                    }
                    R.id.itemAddPhotos -> {
                        Log.i(TAG, "bottomNavigation : PHOTO ADD  ")

                        //
                        if (activeFragment !is PhotoFragment)
                            viewModel.onPhotoSelected()

                        true
                    }
                    R.id.itemProfile -> {
                        Log.i(TAG, "bottomNavigation : PROFILE  ")

                        //
                        if (activeFragment !is ProfileFragment)
                            viewModel.onProfileSelected()


                        true
                    }
                    else -> false
                }
            }
        }


    }

    // You know why I override this, don't you.
    // NOT NEEDED for now.
    override fun setupObservers() {
        super.setupObservers()
        Log.d(TAG, "setupObservers: () ")


        viewModel.homeNavigation.observe(this, {
            Log.i(TAG, "setupObservers: ||||||| onChanged |||||| HOME  ")
            it.getIfNotHandled()?.run { showHome() }
        })


        viewModel.photoNavigation.observe(this, {
            Log.i(TAG, "setupObservers: ||||||| onChanged |||||| PHOTO  ")

            it.getIfNotHandled()?.run { showAddPhoto() }
        })

        viewModel.profileNavigation.observe(this, {
            Log.i(TAG, "setupObservers: ||||||| onChanged |||||| PROFILE  ")

            it.getIfNotHandled()?.run { showProfile() }
        })


        //
        mainSharedViewModel.homeRedirection.observe(this, Observer {
            it.getIfNotHandled()?.run {
                Log.i(
                    TAG,
                    "setupObservers: |||||  onChanged |||||||  REDIRECT from Photo F to HF  "
                )
                // show Home F.
                bottomNavigation.selectedItemId = R.id.itemHome

            }
        })

    }

    /**
     *
     *
     * Used long tern doubt
     *
     * show add and hide combination
     *
     * add good, page don't get loaded unlike replace.
     */
    private fun showHome() {
        Log.d(TAG, "showHome: () ")
        //
        if (activeFragment is HomeFragment) return
        //
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        //
        var fragment = supportFragmentManager.findFragmentByTag(HomeFragment.TAG) as HomeFragment?
        //
        if (fragment == null) {
            fragment = HomeFragment.newInstance()
            fragmentTransaction.add(R.id.containerFragment, fragment, HomeFragment.TAG)
        } else {

            // won't create new instance
            fragmentTransaction.show(fragment)
        }

        //
        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)
        //
        fragmentTransaction.commit()
        //
        activeFragment = fragment
    }


    private fun showAddPhoto() {
        Log.d(TAG, "showAddPhoto: () ")

        if (activeFragment is PhotoFragment) return
        //
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        //
        var fragment = supportFragmentManager.findFragmentByTag(PhotoFragment.TAG) as PhotoFragment?
        //
        if (fragment == null) {
            fragment = PhotoFragment.newInstance()
            fragmentTransaction.add(R.id.containerFragment, fragment, PhotoFragment.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)
        //
        fragmentTransaction.commit()
        //
        activeFragment = fragment
    }

    private fun showProfile() {
        Log.d(TAG, "showProfile: () ")
        //
        if (activeFragment is ProfileFragment) return
        //
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        //
        var fragment =
            supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) as ProfileFragment?
        //
        if (fragment == null) {
            fragment = ProfileFragment.newInstance()
            fragmentTransaction.add(R.id.containerFragment, fragment, ProfileFragment.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }
        //
        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)
        //
        fragmentTransaction.commit()
        //
        activeFragment = fragment
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: () ")
        // not calling onClear, and you don't need to.
        viewModel.clear()
    }
}