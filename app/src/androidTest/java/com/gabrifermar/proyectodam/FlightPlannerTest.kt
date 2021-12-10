package com.gabrifermar.proyectodam

import android.view.View
import android.widget.SeekBar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.gabrifermar.proyectodam.view.FlightPlanner
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class FlightPlannerTest {
    @Rule
    @JvmField
    var activityrule: ActivityScenarioRule<FlightPlanner> =
        ActivityScenarioRule(FlightPlanner::class.java)


    @Test
    fun recyclerview() {

        activityrule.scenario

        onView(withId(R.id.flightplanner_sb_legselector)).perform(setProgress(4))

        onView(withId(R.id.flightplanner_btn_generate)).perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.flightplanner_rv_flightplan)).check(matches(isDisplayed()))

    }

    private fun setProgress(a: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(SeekBar::class.java)
            }

            override fun getDescription(): String {
                return "Set progress value to $a"
            }

            override fun perform(uiController: UiController?, view: View?) {
                val seekBar = view as SeekBar
                seekBar.progress = a
            }
        }
    }

}