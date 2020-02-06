package com.dscvit.periodsapp.utils

import android.os.Looper
import android.util.Log
import com.dscvit.periodsapp.App
import com.google.android.gms.location.*
import kotlin.math.*

class LocationHelper {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(
            App.context
        )
    private val locationRequest: LocationRequest = LocationRequest.create()

    fun getLocation(fLat: Double, fLon: Double) {
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    val location = locationResult.lastLocation
                    val lat = location.latitude
                    val lon = location.longitude
                    Log.d("esh", "FCM: lat:$fLat, lon:$fLon")
                    Log.d("esh", "LOCAL: lat:$lat, lon:$lon")

                    val distance = calculateDistance(fLat, fLon, lat, lon)

                    App.context.shortToast(distance.toString())
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371
        val dLat = degToRad(lat1) - degToRad(lat2)
        val dLon = degToRad(lon1) - degToRad(lon2)
        val temp = sin(dLat/2).pow(2) + cos(degToRad(lat1)) * cos(degToRad(lat2)) * (sin(dLon/2).pow(2))

        return r * (2 * asin(sqrt(temp)))
    }

    private fun degToRad(deg: Double): Double {
        return (deg * PI/180)
    }

}