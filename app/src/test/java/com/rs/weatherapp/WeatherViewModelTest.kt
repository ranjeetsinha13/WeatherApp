package com.rs.weatherapp

import android.app.Application
import com.rs.weatherapp.viewmodel.WeatherViewModel
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.*

class WeatherViewModelTest {

    private val application: Application = mock(Application::class.java)
    private var repoViewModel =
        WeatherViewModel(13.00900900900901, 77.65058459318612, application)

    @Test
    fun testNull() {
        assertThat(repoViewModel.weatherResponse, notNullValue())
    }
}