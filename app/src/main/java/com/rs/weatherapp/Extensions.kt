package com.rs.weatherapp

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

fun IntArray.verifyPermissions() =
    this.isNotEmpty() && this.all { it == PackageManager.PERMISSION_GRANTED }

fun View.getSnackBar(message: String, length: Int) = Snackbar.make(this, message, length)

fun Context.verifyAvailableNetwork(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun Long.toDate(): String? {
    val date = Date(this * 1000L)
    // format of the date
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH)
    sdf.timeZone = TimeZone.getTimeZone(TimeZone.getDefault().id)
    return sdf.format(date)
}

fun Long.toTime(): String? {
    val date = Date(this * 1000L)
    // format of the date
    val sdf = SimpleDateFormat("HH:mm:ss z", Locale.ENGLISH)
    sdf.timeZone = TimeZone.getTimeZone(TimeZone.getDefault().id)
    return sdf.format(date)
}
