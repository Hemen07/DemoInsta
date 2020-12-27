package com.del.demoinsta.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.del.demoinsta.R
import com.del.demoinsta.TestComponentRule
import com.del.demoinsta.data.model.User
import com.del.demoinsta.utils.RVMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class HomeFragmentTest {

    //
    private val TAG = HomeFragmentTest::class.simpleName + " : ";


    // gives you context.
    private val component =
        TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)


    // usual setups
    @get:Rule
    val chain = RuleChain.outerRule(component)

    //
    @Before
    fun setup() {

        /**
         *
         * Simulating a scenario,
         * where HF is being test in isolation, so
         * creating a fake user saving it and calling FakeNS and showing the returned result in HF
         *
         * Because HF expects used to be logged in.
         */


        // get it from .......lol
        val userRepository = component.testComponent!!.getUserRepository()

        // fake user.
        val user = User("userId", "userName", "userEmail", "accessToken", "profilePicUrl")

        //
        userRepository.saveCurrentUser(user)
    }

    /**
     *for testing : fragment, say launching/transaction,
     * make use you add the fragment testing lib.
     */
    @Test
    fun postAvailable_shouldDisplay() {
        Log.d(TAG, "postAvailable_shouldDisplay: () ")

        // empty bundle and style.
        // make sure you specify jvm target 1.8 or else cannot inline bytecode issue will come.
        // kotlinOptions {
        //        jvmTarget = '1.8'
        //    }

        // so open HF
        launchFragmentInContainer<HomeFragment>(Bundle(), R.style.AppTheme)

        // fragment is opened,  now, check RV is visible or not
        Espresso.onView(ViewMatchers.withId(R.id.rvPosts))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))


        // rv is loading correct data from provider FakeNS
        // before that one util RVMatcher is created,
        // This will help matching rv items with the data you want to display
        // so now, below, as per fakeNS, R.id.tvName should show postUserName1 as per my text
        // GOT IT, so in my fakeData, at pos 0 I had "postUserName1" so it is displaying in RV
        // We are testing for R.id.tvName attribute
        Espresso.onView(ViewMatchers.withId(R.id.rvPosts))
            .check(
                ViewAssertions.matches(

                    //
                    RVMatcher.atPositionOnView(
                        0, ViewMatchers.withText("postUserName1"), R.id.tvName
                    )
                )
            )


        // Now, scroll to pos 1 and display postUserName2
        // must add a library espresso contrib
        // lol, it did scroll. hell eyah
        Espresso.onView(ViewMatchers.withId(R.id.rvPosts))
            //
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
            .check(
                ViewAssertions.matches(

                    //
                    RVMatcher.atPositionOnView(
                        1, ViewMatchers.withText("postUserName2"), R.id.tvName
                    )
                )
            )

    }
}