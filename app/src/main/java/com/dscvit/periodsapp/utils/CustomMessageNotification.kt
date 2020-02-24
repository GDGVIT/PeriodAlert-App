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
import androidx.core.content.ContextCompat
import com.dscvit.periodsapp.R
import com.dscvit.periodsapp.ui.chat.ChatActivity

object CustomMessageNotification {

    private const val CHANNEL_ID = "com.dscvit.periodsapp.CHANNEL_ID"
    private const val NOTIFICATION_TAG = "FCM_HELP"
    private const val GROUP_NOTIFICATION = "com.dscvit.periodsapp.GROUP_NOTIFICATION"
    private var mId = 0

    fun notify(context: Context, name: String, msg: String, receiverId: Int, id: Int) {

        mId = id

        val title = "New Message From $name"

        val intent = Intent(context, ChatActivity::class.java)
        intent.putExtra(Constants.EXTRA_RECEIVER_ID, receiverId)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)

            .setDefaults(Notification.DEFAULT_ALL)

            .setSmallIcon(R.drawable.ic_lightdrop)
            .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            .setContentTitle(title)
            .setContentText(msg)

            .setPriority(NotificationCompat.PRIORITY_HIGH)

            .setGroup(GROUP_NOTIFICATION)

            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(msg)
                    .setBigContentTitle(title)
            )

            .setAutoCancel(true)

        val summaryNotification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText("Messages")
            .setSmallIcon(R.drawable.ic_lightdrop)
            .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setGroup(GROUP_NOTIFICATION)
            .setGroupSummary(true)

        notify(context, builder.build(), summaryNotification.build())

    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private fun notify(
        context: Context,
        notification: Notification,
        summaryNotification: Notification
    ) {
        val nm = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(nm)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nm.notify(NOTIFICATION_TAG, mId, notification)
            nm.notify(0, summaryNotification)
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification)
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    fun cancel(context: Context) {
        val nm = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nm.cancel(NOTIFICATION_TAG, mId)
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