package com.del.demoinsta.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import com.del.demoinsta.R
import com.del.demoinsta.di.component.FragmentComponent
import com.del.demoinsta.ui.base.BaseFragment
import com.del.demoinsta.ui.viewmodel.ProfileViewModel

//
@SuppressLint("LogNotTimber")
class ProfileFragment : BaseFragment<ProfileViewModel>() {


    // setting up BaseFragment TAG
    init {

        TAG = ProfileFragment::class.simpleName + " : ";
    }

    //
    companion object {
        //
        internal val TAG = ProfileFragment::class.java.simpleName + " : "

        //
        fun getInstance(fragmentManager: FragmentManager): ProfileFragment {
            Log.e(TAG, "getInstance () : ")
            var profileFragment = fragmentManager.findFragmentByTag(TAG) as ProfileFragment?
            if (profileFragment == null) {
                profileFragment = ProfileFragment()
            }
            return profileFragment
        }

        //
        fun newInstance(): ProfileFragment {
            Log.e(TAG, "newInstance: () ")
            val args = Bundle()
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun provideLayoutId(): Int {
        Log.d(TAG, "provideLayoutId: () ")
        return R.layout.fragment_profile
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        Log.d(TAG, "injectDependencies: () ")
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {
        Log.d(TAG, "setupView: () ")

    }

}
