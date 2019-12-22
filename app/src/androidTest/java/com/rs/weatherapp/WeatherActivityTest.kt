package com.rs.weatherapp

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rs.weatherapp.ui.WeatherActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class WeatherActivityTest {

    @RunWith(AndroidJUnit4::class)
    class MyTestSuite {

        @get:Rule
        var activityScenarioRule =
            ActivityScenarioRule<WeatherActivity>(WeatherActivity::class.java)

        @Test
        fun testWeatherActivityLaunch() {
            val scenario = activityScenarioRule.scenario
            scenario.moveToState(Lifecycle.State.CREATED)
            onView(withId(R.id.weather_summary)).check(matches(isDisplayed()))
        }
    }
}