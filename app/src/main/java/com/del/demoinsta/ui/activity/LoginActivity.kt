package com.del.demoinsta.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import com.del.demoinsta.R
import com.del.demoinsta.di.component.ActivityComponent
import com.del.demoinsta.ui.base.BaseActivity
import com.del.demoinsta.ui.viewmodel.LoginViewModel
import com.del.demoinsta.utils.common.Event
import com.del.demoinsta.utils.common.Status
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 *
 *
 */
@SuppressLint("LogNotTimber")
class LoginActivity : BaseActivity<LoginViewModel>() {

    //
    private val TAG = LoginActivity::class.simpleName + " : ";

    //
    private lateinit var mEmailTextInputLayout: TextInputLayout
    private lateinit var mPwdTextInputLayout: TextInputLayout
    private lateinit var mEmailTextInputEditText: TextInputEditText
    private lateinit var mPwdTextInputEditText: TextInputEditText
    private lateinit var mLoginButton: Button
    private lateinit var mProgressBar: ProgressBar


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
        return R.layout.activity_login
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        Log.d(TAG, "injectDependencies: () ")
        activityComponent.inject(this)
    }


    override fun setupView(savedInstanceState: Bundle?) {
        Log.d(TAG, "setupView: () ")

        //
        mEmailTextInputLayout = findViewById(R.id.layout_email)
        mPwdTextInputLayout = findViewById(R.id.layout_password)
        mEmailTextInputEditText = findViewById(R.id.et_email)
        mPwdTextInputEditText = findViewById(R.id.et_password)
        mLoginButton = findViewById(R.id.bt_login)
        mProgressBar = findViewById(R.id.pb_loading)



        mLoginButton.setOnClickListener {
            Log.w(TAG, "setupView: onClick : Login () ")
            //
            viewModel.onLogin()
        }


        //

        mEmailTextInputEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEmailChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        mPwdTextInputEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onPasswordChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })


    }

    // You know why I override this, don't you.
    // NOT NEEDED for now.
    override fun setupObservers() {
        super.setupObservers()
        Log.d(TAG, "setupObservers: () ")


        // Event is used by the view model to tell the activity to launch another activity
        // view model also provided the Bundle in the event that is needed for the Activity
        viewModel.launchMain.observe(this, Observer<Event<Map<String, String>>> {
            it.getIfNotHandled()?.run {
                Log.i(TAG, "setupObservers: ||||||  onChanged ||||||||()  -> launchDummyMutableLD")
                //
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        })

        //
        viewModel.emailValidation.observe(this, Observer {
            Log.i(TAG, "setupObservers: ||||||  onChanged ||||||||()  -> emailValidationLD")

            when (it.status) {
                Status.ERROR -> mEmailTextInputLayout.error = it.data?.run { getString(this) }
                else -> mEmailTextInputLayout.isErrorEnabled = false
            }
        })
//
        //
        viewModel.emailField.observe(this, Observer {
            Log.i(TAG, "setupObservers: ||||||  onChanged ||||||||()  -> emailFieldMutableLD")

            if (mEmailTextInputEditText.text.toString() != it) mEmailTextInputEditText.setText(it)
        })


        //
        viewModel.passwordField.observe(this, Observer {
            Log.i(TAG, "setupObservers: ||||||  onChanged ||||||||()  -> passwordFieldMutableLD")

            if (mPwdTextInputEditText.text.toString() != it) mPwdTextInputEditText.setText(it)
        })


//
        viewModel.passwordValidation.observe(this, Observer {
            Log.i(TAG, "setupObservers: ||||||  onChanged ||||||||()  -> passwordValidationLD")

            when (it.status) {
                Status.ERROR -> mPwdTextInputLayout.error = it.data?.run { getString(this) }
                else -> mPwdTextInputLayout.isErrorEnabled = false
            }
        })

        viewModel.loggingIn.observe(this, Observer {
            Log.i(TAG, "setupObservers: ||||||  onChanged ||||||||()  -> loggingInMutableLD")

            mProgressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
//

    }


    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: () ")
        // not calling onClear, and you don't need to.
        // this just a dummy.
        viewModel.clear()
    }
}