package com.dscvit.periodsapp.firebase

import android.util.Log
import com.dscvit.periodsapp.utils.LocationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.core.KoinComponent

class MyFirebaseMessagingService : FirebaseMessagingService(), KoinComponent {

    private val locationHelper = LocationHelper()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val lon = remoteMessage.data["lon"]
        val lat = remoteMessage.data["lat"]

        //Log.d("esh", "FCM: lat:$lat, lon:$lon")

        locationHelper.getLocation(lat!!.toDouble(), lon!!.toDouble())

    }

    override fun onNewToken(p0: String) {
        // Update the new token in backend
        Log.d("esh", p0)
    }

}