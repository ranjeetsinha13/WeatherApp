package com.rs.weatherapp.weatherrepo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.rs.weatherapp.network.RetrofitFactory.retrofitService
import com.rs.weatherapp.network.RetrofitService
import com.rs.weatherapp.network.data.ApiError
import com.rs.weatherapp.network.data.Resource
import com.rs.weatherapp.network.data.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository(context: Context) {

    private val weatherResponse: MutableLiveData<Resource<WeatherResponse>> = MutableLiveData()
    private val ERROR_CODE: Int = 101

    private val retrofitApi: RetrofitService by lazy {
        retrofitService(context)
    }

    fun getWeatherResponse(
        latitude: Double,
        longitude: Double
    ): MutableLiveData<Resource<WeatherResponse>> {
        retrofitApi.getWeather(latitude, longitude).enqueue(object :
            Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                weatherResponse.postValue(
                    Resource.error(
                        ApiError(
                            t.localizedMessage ?: "",
                            ERROR_CODE
                        )
                    )
                )
            }

            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    weatherResponse.postValue(Resource.success(response.body()))
                } else {
                    weatherResponse.postValue(
                        Resource.error(
                            ApiError(
                                "Please check Internet connection",
                                ERROR_CODE
                            )
                        )
                    )
                }
            }
        }
        )
        return weatherResponse
    }
}