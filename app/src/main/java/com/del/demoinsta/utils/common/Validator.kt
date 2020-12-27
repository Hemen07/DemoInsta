package com.del.demoinsta.utils.common

import android.annotation.SuppressLint
import android.util.Log
import com.del.demoinsta.R
import java.util.regex.Pattern

/**
 *
 * It's Responsibility is to Validate.
 *
 */
@SuppressLint("LogNotTimber")
object Validator {

    private val TAG = Validator::class.simpleName + " : ";

    // Email REGEX
    private val EMAIL_ADDRESS = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    //
    private const val MIN_PASSWORD_LENGTH = 6


    /**
     * Returns list of [Validation] for email and password
     *
     * Input can be null, and they are being processed too.
     * @param email
     * @param password
     */
    fun validateLoginFields(email: String?, password: String?): List<Validation> {
        Log.d(TAG, "validateLoginFields: () ")

        return ArrayList<Validation>().apply {

            // For Email.
            when {
                //
                email.isNullOrBlank() ->

                    add(
                        Validation(
                            Validation.Field.EMAIL,
                            Resource.error(R.string.email_field_empty)
                        )
                    )
                //
                !EMAIL_ADDRESS.matcher(email).matches() ->
                    add(
                        Validation(
                            Validation.Field.EMAIL,
                            Resource.error(R.string.email_field_invalid)
                        )
                    )
                //
                else ->
                    add(Validation(Validation.Field.EMAIL, Resource.success()))
            }

            // For Password
            when {

                //
                password.isNullOrBlank() ->
                    add(
                        Validation(
                            Validation.Field.PASSWORD,
                            Resource.error(R.string.password_field_empty)
                        )
                    )

                //
                password.length < MIN_PASSWORD_LENGTH ->
                    add(
                        Validation(
                            Validation.Field.PASSWORD,
                            Resource.error(R.string.password_field_small_length)
                        )
                    )
                //
                else -> add(Validation(Validation.Field.PASSWORD, Resource.success()))
            }
        }
    }
}


/**
 * @param field : enums
 * @param resource : [Resource] type is Int as in string resource.
 */
data class Validation(val field: Field, val resource: Resource<Int>) {

    //
    enum class Field {
        EMAIL,
        PASSWORD
    }
}
