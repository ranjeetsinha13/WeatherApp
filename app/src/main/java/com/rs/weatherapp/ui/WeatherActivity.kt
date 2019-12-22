package com.rs.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.rs.weatherapp.getSnackBar
import com.rs.weatherapp.verifyPermissions
import com.rs.weatherapp.R

class WeatherActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val TAG = WeatherActivity::class.java.simpleName

    companion object {
        private val PERMISSIONS_LOCATION = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        private const val PERMISSION_LOCATION_REQUEST_CODE = 12
        private const val ENABLE_LOCATION_REQUEST_CODE = 13
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.weather_layout)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            requestLocationPermissions()
        } else {
            getLastLocation()
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            findViewById<View>(android.R.id.content).getSnackBar(
                getString(R.string.permission_location_rationale),
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.ok) {
                    ActivityCompat
                        .requestPermissions(
                            this@WeatherActivity, PERMISSIONS_LOCATION,
                            PERMISSION_LOCATION_REQUEST_CODE
                        )
                }
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_LOCATION,
                PERMISSION_LOCATION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_LOCATION_REQUEST_CODE) {
            if (grantResults.verifyPermissions()) {
                // Permission granted
                getLastLocation()
            } else {
                // Permission not granted
                findViewById<View>(android.R.id.content).getSnackBar(
                    getString(R.string.permission_not_granted),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        val builder = LocationSettingsRequest.Builder()
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            if (!it.locationSettingsStates.isLocationUsable) {
                // Ask user to enable location
                enableLocation()
            } else {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        // get data and populate UI
                        startWeatherFragment(location.latitude, location.longitude)
                    }
                }
            }
        }
            .addOnFailureListener {
                Log.w(TAG, "Location failure", it)
                if (it is ResolvableApiException) {
                    try {
                        it.startResolutionForResult(
                            WeatherActivity@ this,
                            ENABLE_LOCATION_REQUEST_CODE
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
    }

    private fun startWeatherFragment(latitude: Double, longitude: Double) {
        var fragment = supportFragmentManager.findFragmentByTag(WeatherFragment::class.java.name)
        if (fragment == null) {
            fragment = WeatherFragment.newInstance(latitude, longitude)
            supportFragmentManager.beginTransaction().replace(
                R.id.frame_layout,
                fragment,
                WeatherFragment::class.java.name).commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun enableLocation() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.enable_location_settings))
            .setCancelable(true)
            .setPositiveButton(R.string.ok) { _: DialogInterface, i: Int ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton(R.string.cancel) { dialog: DialogInterface, i: Int ->
                dialog.cancel()
            }
            .setTitle(getString(R.string.location_settting_title))
            .create()
            .show()
    }
}