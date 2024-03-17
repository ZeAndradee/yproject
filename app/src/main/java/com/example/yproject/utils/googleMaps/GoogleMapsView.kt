package com.example.yproject.utils.googleMaps

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

private lateinit var fusedLocationClient: FusedLocationProviderClient

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun checkAndRequestLocationPermission(context: Context) {
    if (!context.hasLocationPermission()) {
        ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
    }
}

fun Activity.getLastLocation(callback: (LatLng?) -> Unit) {
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    if (this.hasLocationPermission()) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    var currentPosition = LatLng(location.latitude, location.longitude)
                    Log.d("Location", "Lat: ${location.latitude}, Lon: ${location.longitude}")
                    callback(currentPosition)
                }
            }
    } else {
        // Solicitar permissão
        checkAndRequestLocationPermission(this)
    }
}


