package com.rs.weatherapp.network.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    @SerializedName("currently")
    val weatherDetails: CurrentWeatherDetail,
    @SerializedName("hourly")
    val hourlyWeatherList: WeatherDetail,
    @SerializedName("daily")
    val dailyWeatherDetail: WeatherDetail,
    val alerts: List<Alert>?,
    val flags: Flag,
    val offset: Double?
)

data class CurrentWeatherDetail(

    val time: Long,
    val summary: String,
    val icon: String,
    val precipIntensity: Double?,
    val precipProbability: Double?,
    val precipType: String?,
    val temperature: Double,
    val apparentTemparature: Double?,
    val dewPoint: Double?,
    val humidity: Double?,
    val pressure: Double?,
    val windSpeed: Double?,
    val windGust: Double?,
    val windBearing: Int?,
    val cloudCover: Double?,
    val uVIndex: Int?,
    val visibility: Double?,
    val ozone: Double?
)

data class WeatherDetail(
    val summary: String,
    val icon: String,
    val data: List<CurrentWeatherDetail>?
)

data class Alert(
    val title: String?,
    val regions: List<String?>?,
    val severity: String?,
    val time: Long?,
    val expires: Long?,
    val description: String?,
    val uri: String?
)

data class Flag(
    val sources: List<String?>?,
    @SerializedName("meteoalarm-license")
    val meteoAlarmLicense: String?,
    @SerializedName("nearest-station")
    val nearestStation: Double?,
    val units: String
)
