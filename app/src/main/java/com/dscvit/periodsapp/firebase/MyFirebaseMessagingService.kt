package com.dscvit.periodsapp.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("esh", remoteMessage.data["lat"] + remoteMessage.data["lon"])
    }

    override fun onNewToken(p0: String) {
        // Update the new token in backend
        Log.d("esh", p0)
    }

}