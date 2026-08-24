package com.martorell.albert.meteomartocompose.framework.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.martorell.albert.meteomartocompose.MainActivity
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.data.notification.NotificationService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationServiceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager,
) : NotificationService {

    init {
        createNotificationChannel()
    }

    override fun showHighTemperatureNotification(temperature: Double) {
        Log.d(TAG, "Attempting to show high temperature notification for $temperature°C")
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, we can't show the notification
            Log.e(TAG, "Notification failed: POST_NOTIFICATIONS permission NOT granted at runtime")
            return
        }

        val title = context.getString(R.string.high_temp_notif_title)
        val content = context.getString(R.string.high_temp_notif_content, temperature)

        // Create the deep link intent
        val deepLinkUri = Uri.parse("$DEEP_LINK_BASE/$temperature")
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            deepLinkUri,
            context,
            MainActivity::class.java
        )

        // Use TaskStackBuilder to ensure back navigation works correctly
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
        Log.i(TAG, "Notification successfully sent to system tray")
    }

    private fun createNotificationChannel() {
        val name = context.getString(R.string.high_temp_notif_channel_name)
        val descriptionText = context.getString(R.string.high_temp_notif_channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val TAG = "NotificationService"
        private const val CHANNEL_ID = "high_temp_channel"
        private const val NOTIFICATION_ID = 1001
        private const val DEEP_LINK_BASE = "meteomarto://alert/high-temperature"
    }

}
