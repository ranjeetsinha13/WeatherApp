package com.rs.weatherapp.network

import com.rs.weatherapp.network.data.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {
    @GET("{latitude},{longitude}")
    fun getWeather(@Path("latitude") lat: Double, @Path("longitude") lng: Double): Call<WeatherResponse>
}