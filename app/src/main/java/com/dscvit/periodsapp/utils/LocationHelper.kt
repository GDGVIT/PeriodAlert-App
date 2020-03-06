package com.dscvit.periodsapp.utils

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.dscvit.periodsapp.App
import com.dscvit.periodsapp.model.requests.Request
import com.dscvit.periodsapp.repository.AppRepository
import kotlinx.coroutines.runBlocking
import java.text.DecimalFormat
import java.util.*
import kotlin.math.*


class LocationHelper {

    @SuppressWarnings("MissingPermission")
    fun getLocationAndNotify(fLat: Double, fLon: Double, receiverId: Int, userName: String) {
        val locationManager =
            App.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestSingleUpdate(
            LocationManager.NETWORK_PROVIDER,
            object : LocationListener {
                override fun onLocationChanged(location: Location?) {
                    if (location != null) {
                        val lat = location.latitude
                        val lon = location.longitude
                        Log.d("esh", "FCM: lat:$fLat, lon:$fLon")
                        Log.d("esh", "LOCAL: lat:$lat, lon:$lon")

                        val distance = calculateDistance(fLat, fLon, lat, lon)
                        val df = DecimalFormat("#.##")
                        val distanceToShow = df.format(distance)

                        if (distance <= 5) {
                            val id = (Date().time / 1000L % Int.MAX_VALUE).toInt()
                            CustomLocationNotification.notify(
                                App.context,
                                "Approx Distance: $distanceToShow KM",
                                id,
                                receiverId,
                                userName
                            )
                        }
                    }
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                override fun onProviderEnabled(provider: String?) {}

                override fun onProviderDisabled(provider: String?) {}
            },
            Looper.getMainLooper()
        )
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371
        val dLat = degToRad(lat1) - degToRad(lat2)
        val dLon = degToRad(lon1) - degToRad(lon2)
        val temp =
            sin(dLat / 2).pow(2) + cos(degToRad(lat1)) * cos(degToRad(lat2)) * (sin(dLon / 2).pow(2))

        return r * (2 * asin(sqrt(temp)))
    }

    private fun degToRad(deg: Double): Double {
        return (deg * PI / 180)
    }

}