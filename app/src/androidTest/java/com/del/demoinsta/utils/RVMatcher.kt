package com.del.demoinsta.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 *
 *
 * Class to match the data in the RV with the text which you want to represent.
 *
 * so, it means, It will help to display data you have from NS in RV and verify
 *
 */
object RVMatcher {

    fun atPositionOnView(

        position: Int,
        itemMatcher: Matcher<View>,
        targetViewId: Int

    ): Matcher<View> {


        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {


            override fun describeTo(description: Description) {
                description.appendText("has view if $itemMatcher at position $position")
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {

                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                val targetView = viewHolder!!.itemView.findViewById<View>(targetViewId)
                return itemMatcher.matches(targetView)
            }


        }
    }
}

