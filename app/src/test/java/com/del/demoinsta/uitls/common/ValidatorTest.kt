package com.del.demoinsta.uitls.common

import android.util.Log
import com.del.demoinsta.R
import com.del.demoinsta.utils.common.Resource
import com.del.demoinsta.utils.common.Validation
import com.del.demoinsta.utils.common.Validator
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.hasSize
import org.junit.*
import org.junit.Assert.assertThat
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

/**
 **
 * Robolectric
 */

@RunWith(RobolectricTestRunner::class)
@Config(shadows = [ShadowLog::class])
class ValidatorTest {

    companion object {

        private val TAG = ValidatorTest::class.java.simpleName + " : "

        @BeforeClass
        @kotlin.jvm.JvmStatic
        fun setUp() {
            println("|||||| setUp ||||||||")
            //
            ShadowLog.setupLogging()

        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            println("------- tearDown ---------- ")
        }
    }




    /**
     * target is validateLoginFields method.
     */
    @Test
    fun givenValidEmailAndValidPwd_whenValidate_shouldReturnSuccess() {
        Log.d(TAG, "givenValidEmailAndValidPwd_whenValidate_shouldReturnSuccess: () ")


        // valid email
        val email = "test@gamil.com"

        // valid pwd
        val pwd = "password"

        // let's call
        val validationList: List<Validation> = Validator.validateLoginFields(email, pwd)

        // using hamcrest as per them, better assertions
        // assert : size
        // we know the size should be 2, or else fail
        MatcherAssert.assertThat(validationList, Matchers.hasSize(2))

        // make sure, override hashy or else different object id, you will get
        // if using data class, no need to worry.
        MatcherAssert.assertThat(
            //
            validationList,

            //
            Matchers.contains(
                //
                Validation(Validation.Field.EMAIL, Resource.success()),
                //
                Validation(Validation.Field.PASSWORD, Resource.success())
            )
        )
    }


    /**
     * target is validateLoginFields method.
     */
    @Test
    fun givenInvalidEmailAndValidPwd_whenValidate_shouldReturnEmailError() {
        Log.d(TAG, "givenInvalidEmailAndValidPwd_whenValidate_shouldReturnEmailError: () ")

        val email = "test.com"
        val password = "password"
        val validations = Validator.validateLoginFields(email, password)
        assertThat(validations, Matchers.hasSize(2))
        assertThat(
            validations,
            Matchers.contains(
                Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_invalid)),
                Validation(Validation.Field.PASSWORD, Resource.success())
            )
        )
    }

    /**
     * target is validateLoginFields method.
     */
    @Test
    fun givenValidEmailAndInvalidPwd_whenValidate_shouldReturnPasswordError() {
        Log.d(TAG, "givenValidEmailAndInvalidPwd_whenValidate_shouldReturnPasswordError: () ")

        val email = "test@gmail.com"
        val password = "pwd"
        val validations = Validator.validateLoginFields(email, password)
        assertThat(validations, hasSize(2))
        assertThat(
            validations,
            contains(
                Validation(Validation.Field.EMAIL, Resource.success()),
                Validation(
                    Validation.Field.PASSWORD,
                    Resource.error(R.string.password_field_small_length)
                )
            )
        )
    }
}
