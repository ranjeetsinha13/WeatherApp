package com.rs.weatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rs.weatherapp.network.data.Resource
import com.rs.weatherapp.network.data.WeatherResponse
import com.rs.weatherapp.weatherrepo.WeatherRepository

class WeatherViewModel(latitude: Double, longitude: Double, application: Application) :
    AndroidViewModel(application) {

    private val weatherRepository by lazy {
        WeatherRepository(application)
    }

    val weatherResponse: MutableLiveData<Resource<WeatherResponse>> by lazy {
        weatherRepository.getWeatherResponse(latitude, longitude)
    }

    fun refreshData(
        latitude: Double,
        longitude: Double
    ): MutableLiveData<Resource<WeatherResponse>> {
        return weatherRepository.getWeatherResponse(latitude, longitude)
    }
}