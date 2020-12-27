package com.del.demoinsta.ui.activity

import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.del.demoinsta.R
import com.del.demoinsta.TestComponentRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

/**
 *
 *
 *
 */
class LoginActivityTest {

    private val TAG = LoginActivityTest::class.simpleName + " : ";


    // gives you context.
    private val component =
        TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    //
    @get:Rule
    val chain = RuleChain.outerRule(component)


    //
    @Before
    fun setup() {
        // don't need anything for now
    }


    /**
     *
     */
    @Test
    fun testCheckViewsDisplay() {
        Log.d(TAG, "testCheckViewsDisplay: () ")

        //
        ActivityScenario.launch(LoginActivity::class.java)

        /**
         * verify
         * email, pass and btn is visible or not.
         *
         */
        onView(withId(R.id.layout_email))
            .check(matches(isDisplayed()))

        onView(withId(R.id.layout_password))
            .check(matches(isDisplayed()))

        onView(withId(R.id.bt_login))
            .check(matches(isDisplayed()))
    }


    @Test
    fun givenValidEmailAndPass_whenLogin_shouldLaunchMainActivity() {
        Log.d(TAG, "givenValidEmailAndPass_whenLogin_shouldLaunchMainActivity: () ")

        // we will not use real NS, rather FakeNS, advised to use

        // launch Login first
        ActivityScenario.launch(LoginActivity::class.java)

        // email simulation for click, espresso will handle typing and closing keyboard

        // no need to put original, as I am hitting fakeNH and fakeNS
        Espresso.onView(withId(R.id.et_email))
            .perform(
                ViewActions.typeText("test@gmail.com"),
                ViewActions.closeSoftKeyboard()
            )

        Espresso.onView(withId(R.id.et_password))
            .perform(
                ViewActions.typeText("pass123"),
                ViewActions.closeSoftKeyboard()
            )


        // now, onClick.
        // so since we defined FAKE NS and FAKE NH in applicationTestModule
        // so our test gonna use those class.
        Espresso.onView(withId(R.id.bt_login))
            .perform(
                ViewActions.click()
            )


        // verify.
        Espresso.onView(withId(R.id.bottomNavigation))
            .check(matches(isDisplayed()))


    }
}