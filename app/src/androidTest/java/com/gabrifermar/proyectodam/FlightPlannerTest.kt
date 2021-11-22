package com.gabrifermar.proyectodam

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
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
    fun setUp() {
        activityrule.scenario

        onView(withId(R.id.flightplanner_btn_generate)).perform(click())
        //onView(withId(R.id.flightplanner_tv_seekbarvalue)).check()

    }


}