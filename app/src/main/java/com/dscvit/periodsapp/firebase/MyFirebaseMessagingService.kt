package com.dscvit.periodsapp.firebase

import android.util.Log
import com.dscvit.periodsapp.utils.Constants
import com.dscvit.periodsapp.utils.LocationHelper
import com.dscvit.periodsapp.utils.PreferenceHelper
import com.dscvit.periodsapp.utils.PreferenceHelper.set
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val locationHelper = LocationHelper()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val lon = remoteMessage.data["lon"]
        val lat = remoteMessage.data["lat"]
        val user_id = remoteMessage.data["user_id"]

        Log.d("esh", "FCM Received")

        locationHelper.getLocationAndNotify(lat!!.toDouble(), lon!!.toDouble())
    }

    override fun onNewToken(token: String) {
        // Update the new token in backend
        Log.d("esh", token)

        val sharedPreferences = PreferenceHelper.customPrefs(this, Constants.PREF_NAME)
        sharedPreferences[Constants.PREF_FCM_TOKEN] = token
    }

}