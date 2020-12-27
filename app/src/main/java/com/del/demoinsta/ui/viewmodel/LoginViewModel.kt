package com.del.demoinsta.ui.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.del.demoinsta.data.repository.UserRepository
import com.del.demoinsta.ui.base.BaseViewModel
import com.del.demoinsta.utils.common.*
import com.del.demoinsta.utils.network.NetworkHelper
import com.del.demoinsta.utils.rx.SchedulerProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 *
 */
@SuppressLint("LogNotTimber")
class LoginViewModel(
    //
    schedulerProvider: SchedulerProvider,
    //
    compositeDisposable: CompositeDisposable,
    //
    networkHelper: NetworkHelper,
    //
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    //
    private val TAG = LoginViewModel::class.simpleName + " : ";


    // not observing this ld, just storing
    private val validationsList: MutableLiveData<List<Validation>> = MutableLiveData()

    //    val launchDummy: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val launchMain: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()


    val emailField: MutableLiveData<String> = MutableLiveData()
    val passwordField: MutableLiveData<String> = MutableLiveData()
    val loggingIn: MutableLiveData<Boolean> = MutableLiveData()

    val emailValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.EMAIL)
    val passwordValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.PASSWORD)


    //
    init {
        Log.i(TAG, "init()")
    }

    override fun notifyOnCreateFinished() {
        Log.d(TAG, "notifyOnCreateFinished: () ")
    }

    fun onEmailChange(email: String): LiveData<String> {
        Log.d(TAG, "onEmailChange: () input = $email")
        //
        emailField.postValue(email)
        return emailField
    }


    fun onPasswordChange(pass: String): LiveData<String> {
        Log.d(TAG, "onPasswordChange: () input = $pass")
        //
        passwordField.postValue(pass)
        return passwordField
    }

//


    /**
     *
     */
    fun onLogin() {
        Log.d(TAG, "onLogin: () ")
        //
        val email = emailField.value
        val password = passwordField.value

        //
        val validationsList = Validator.validateLoginFields(email, password)
        this.validationsList.postValue(validationsList)

        //
        if (validationsList.isNotEmpty() && email != null && password != null) {

            //
            val successValidationList =
                validationsList.filter { it.resource.status == Status.SUCCESS }
            //
            if (successValidationList.size == validationsList.size && checkInternetConnectionWithMessage()) {

                // tell the progressbar to load
                loggingIn.postValue(true)

                //
                compositeDisposable.addAll(
                    userRepository.doUserLogin(email, password)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe(
                            {
                                //
                                Log.i(TAG, "onLogin: () $it")
                                userRepository.saveCurrentUser(it)
                                // progress bar to stop
                                loggingIn.postValue(false)
                                launchMain.postValue(Event(emptyMap()))
                            },
                            {
                                //
                                handleNetworkError(it)
                                // progress bar to stop
                                loggingIn.postValue(false)
                            }
                        )
                )
            }
            //
        }
    }

    /**
     *
     * Return the Observable/LD and type is Resource<Int>
     *
     *
     * same can be carried out by Rx map
     *
     */
    private fun filterValidation(field: Validation.Field): LiveData<Resource<Int>> {
        Log.d(TAG, "filterValidation: (), field = ${field.name}")


        // it takes LD as param,
        // just like Rx Observable.fromIterable, which also uses source.
        // Function - takes IP and return OP
        return Transformations.map(validationsList, Function {

            // we have

            //Returns the first element matching the given predicate, or null if no such element was found.
            it.find {

                // first item we have, gotta return boolean.
                    validation ->
                validation.field == field

            }
                // found the Validation item  via find()
                // you can use if else too for null check
                ?.run {

                    // this : Validation
                    return@run this.resource

                }
                ?: Resource.unknown()  // else, if Validation is null return unknown. elvis operator.

        })

    }


    // don't worry not calling onClear, BVM handling that
    // this is just specific to this class.
    fun clear() {
        Log.d(TAG, "clear: () ")
        // not needed
        //  compositeDisposable.clear()
    }
}
