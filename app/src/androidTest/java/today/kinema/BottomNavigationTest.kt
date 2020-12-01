/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package today.kinema

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoActivityResumedException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import junit.framework.TestCase.fail
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test


class BottomNavigationTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun bottomNavView_clickOnAllItems() {
        // All screens open at their first destinations
        assertFirstScreen()

        openSecondScreen()

        assertSecondScreen()

        openThirdScreen()

        assertThirdScreen()

        openFirstScreen()

        assertFirstScreen()
    }

    @Test
    fun bottomNavView_backGoesToFirstItem() {
        // From the 2nd or 3rd screens, back takes you to the 1st.
        openThirdScreen()

        pressBack()

        assertFirstScreen()
    }

    @Test(expected = NoActivityResumedException::class)
    fun bottomNavView_backfromFirstItemExits() {
        // From the first screen, back finishes the activity
        assertFirstScreen()

        pressBack() // This should throw NoActivityResumedException

        fail() // If it doesn't throw
    }

    @Test
    fun bottomNavView_backstackMaintained() {
        // The back stack of any screen is maintained when returning to it
        openSecondScreen()

        //click in a watchlist element
        onView(withId(R.id.rvWatchlist))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        assertDeeperSecondScreen()

        openFirstScreen()

        // Return to 3rd
        openSecondScreen()

        // Assert it maintained the back stack
        assertDeeperSecondScreen()
    }
//    @Test
//    fun scrollToItemBelowFold_checkItsText() {
//        openSecondScreen()
//
//        // First, scroll to the position that needs to be matched and click on it.
//        onView(withId(R.id.rvWatchlist))
//            .perform(
//                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                    0,
//                    click()
//                )
//            )
//
////        // Match the text in an item below the fold and check that it's displayed.
////        val itemElementText = "9QE8kN31"
////        onView(withText(itemElementText)).check(matches(isDisplayed()))
//
//        assertMovieDetailsScreen()
//
//    }
//
//    @Test
//    fun bottomNavView_registerBackRegister() {
//        openThirdScreen()
//
//        pressBack() // This is handled in a especial way in code.
//
//        openThirdScreen()
//
//        onView(withContentDescription(R.string.sign_up))
//            .perform(click())
//
//        // Assert it maintained the back stack
//        assertDeeperThirdScreen()
//    }

    @Test
    fun bottomNavView_watchlistBackWatchlistAndMovieDetails() {
        openSecondScreen()
        pressBack()
        assertFirstScreen()
        openSecondScreen()

        //click in a watchlist element
        onView(withId(R.id.rvWatchlist))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        assertDeeperSecondScreen()
    }

    @Test
    fun bottomNavView_moviesMovieDetailsBackMovies() {
        openFirstScreen()
        assertFirstScreen()

        //click in a movie from list
        onView(withId(R.id.movie_list_grid))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        assertDeeperFirstScreen()
        pressBack()
        assertFirstScreen()
    }

    //
    @Test
    fun bottomNavView_itemReselected_goesBackToStart() {
        openSecondScreen()

        assertSecondScreen()

        //click in a watchlist element
        onView(withId(R.id.rvWatchlist))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        assertDeeperSecondScreen()

        // Reselect the current item
        openSecondScreen()

        // Verify that it popped the back stack until the start destination.
        assertSecondScreen()
    }
}

private fun assertSecondScreen() {
//    onView(allOf(withText(R.string.watchlist_title)))
//        .check(matches(isDisplayed()))

    onView(withId(R.id.toolbar))
        .check(matches(hasDescendant(withText(R.string.watchlist_title))))
//    onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.toolbar))))
//        .check(matches(withText("Watchlist")))

}

private fun openSecondScreen() {
    onView(allOf(withContentDescription(R.string.menu_item_watchlist), isDisplayed()))
        .perform(click())
}

private fun assertDeeperSecondScreen() {
    assertDeeperFirstScreen()
}

private fun assertDeeperFirstScreen() {
    onView(withId(R.id.toolbar))
        .check(matches(hasDescendant(withText(R.string.movie_details_title))));
}

private fun openFirstScreen() {
    onView(allOf(withContentDescription(R.string.menu_item_home), isDisplayed()))
        .perform(click())
}

private fun assertFirstScreen() {
    onView(withId(R.id.movie_list_grid))
        .check(matches(isDisplayed()))
}

private fun openThirdScreen() {
    onView(allOf(withContentDescription(R.string.menu_item_filter), isDisplayed()))
        .perform(click())
}

private fun assertThirdScreen() {
    onView(withId(R.id.toolbar))
        .check(matches(hasDescendant(withText(R.string.filter_title))));

}



