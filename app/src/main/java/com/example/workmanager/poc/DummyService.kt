package com.example.workmanager.poc

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class DummyService: Service() {

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotification()
    }

    private fun showNotification(builder: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
//            notify(88, builder.build())
            startForeground(90, builder.build())
        }
    }

    private fun createNotification() {
        val channelID = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) "CHANNEL_ID" else null
        var builder = if (channelID.isNullOrEmpty()) NotificationCompat.Builder(this) else NotificationCompat.Builder(this, channelID)
        builder
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Test service")
            .setContentText("should stay sticky till time period ends")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(false)
            .setOngoing(true)
        showNotification(builder)
    }
}