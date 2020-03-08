package com.dscvit.periodsapp.firebase

import android.util.Log
import com.dscvit.periodsapp.App
import com.dscvit.periodsapp.utils.*
import com.dscvit.periodsapp.utils.PreferenceHelper.set
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val locationHelper = LocationHelper()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (!remoteMessage.data["lon"].isNullOrEmpty()) {

            Log.d("esh", "Other location received")

            val lon = remoteMessage.data["lon"]
            val lat = remoteMessage.data["lat"]
            val userId = remoteMessage.data["user_id"]
            val userName = remoteMessage.data["user_name"]

            locationHelper.getLocationAndNotify(
                lat!!.toDouble(),
                lon!!.toDouble(),
                userId!!.toInt(),
                userName!!
            )
        }

        /*
        if(!remoteMessage.data["sender_name"].isNullOrEmpty()) {
            val id = (Date().time / 1000L % Int.MAX_VALUE).toInt()
            val name = remoteMessage.data["sender_name"]
            val message = remoteMessage.data["body"]
            val senderId = remoteMessage.data["sender_id"]
            val receiverId = remoteMessage.data["receiver_id"]

            CustomMessageNotification.notify(
                App.context,
                name!!,
                message!!,
                senderId!!.toInt(),
                id
            )
        }
         */

        Log.d("esh", "FCM Received")
    }

    override fun onNewToken(token: String) {
        // Update the new token in backend
        Log.d("esh", token)

        val sharedPreferences = PreferenceHelper.customPrefs(this, Constants.PREF_NAME)
        sharedPreferences[Constants.PREF_FCM_TOKEN] = token
    }

}