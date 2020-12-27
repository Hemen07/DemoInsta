package com.del.demoinsta

import android.content.Context
import android.util.Log
import com.del.demoinsta.di.component.DaggerTestComponent
import com.del.demoinsta.di.component.TestComponent
import com.del.demoinsta.di.module.ApplicationTestModule
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 *
 * Rules that are needed for setting up Dagger.
 *
 */
class TestComponentRule(private val context: Context) : TestRule {

    //
    private val TAG = TestComponentRule::class.simpleName + " : ";


    // instance of TestComponent.
    var testComponent: TestComponent? = null

    // getter for context
    fun getContext() = context


    /*
    *
     */
    override fun apply(base: Statement, description: Description?): Statement {
        Log.d(TAG, "apply: () ")

        return object : Statement() {

            @Throws(Throwable::class)
            override fun evaluate() {

                try {
                    setUpDaggerTestComponentInApplication()
                    base.evaluate()
                } finally {
                    testComponent = null
                }
            }
        }
    }


    /**
     * Method needed to setup Dagger
     */
    private fun setUpDaggerTestComponentInApplication() {
        Log.d(TAG, "setUpDaggerTestComponentInApplication: () ")

        // application instance
        val application = context.applicationContext as InstagramApplication
        //
        testComponent = DaggerTestComponent.builder()
            .applicationTestModule(ApplicationTestModule(application))
            .build()
        application.applicationComponent = testComponent!!
    }
}