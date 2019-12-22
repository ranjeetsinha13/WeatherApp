package com.rs.weatherapp.network

import android.content.Context
import com.rs.weatherapp.verifyAvailableNetwork
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {
    private const val BASE_URL =
        "https://api.darksky.net/forecast/2bb07c3bece89caf533ac9a5d23d8417/"

    fun retrofitService(context: Context): RetrofitService {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RetrofitService::class.java)
    }

    private fun createOkHttpClient(context: Context): OkHttpClient {

        val cacheSize = (10 * 1024).toLong()
        val cache = Cache(context.cacheDir, cacheSize)

        return OkHttpClient.Builder()
            .addInterceptor(createLoggingInterceptor())
            .cache(cache)
            .addInterceptor {
                var request = it.request()
                request = if (context.verifyAvailableNetwork())
                    request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build()
                else
                    request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
                it.proceed(request)
            }
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level =
            HttpLoggingInterceptor.Level.BODY
        return logging
    }
}