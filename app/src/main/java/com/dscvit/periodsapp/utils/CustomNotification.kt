package com.dscvit.periodsapp.utils

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.ui.PostAuthActivity

object CustomNotification {

    private const val CHANNEL_ID = "com.dscvit.periodsapp.CHANNEL_ID"
    private const val NOTIFICATION_TAG = "FCM_HELP"

    fun notify(context: Context, text: String) {

        val title = "Request to help"

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)

            .setDefaults(Notification.DEFAULT_ALL)

            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)

            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, PostAuthActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT
                ))

            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(text)
                .setBigContentTitle(title)
                .setSummaryText("Someone requested to help"))

            .setAutoCancel(true)

        notify(context, builder.build())

    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private fun notify(context: Context, notification: Notification) {
        val nm = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(nm)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nm.notify(NOTIFICATION_TAG, 0, notification)
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification)
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    fun cancel(context: Context) {
        val nm = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nm.cancel(NOTIFICATION_TAG, 0)
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode())
        }
    }

    private fun createChannel(nm: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Messages"
            val description = "Notifies when we wan't to send you some messages"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            nm.createNotificationChannel(channel)
        }
    }

}