package com.rs.weatherapp.network.data

class Resource<T> private constructor(
    val status: Status,
    val data: T?,
    val apiError: ApiError?
) {
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        fun <T> success(data: T?): Resource<T> = Resource(Status.SUCCESS, data, null)

        fun <T> error(apiError: ApiError?): Resource<T> = Resource(Status.ERROR, null, apiError)

        fun <T> loading(data: T?): Resource<T> = Resource(Status.LOADING, data, null)
    }
}

class ApiError(val message: String, val errorCode: Int)