package com.example.workmanager.poc

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class DummyService: Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotification()
        getLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.interval = 10000
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult ?: return
                for (location in locationResult.locations) {
                    Toast.makeText(this@DummyService,"latitude: ${location.latitude} longitude: ${location.longitude}", Toast.LENGTH_SHORT).show()
                    println("latitude: ${location.latitude} longitude: ${location.longitude}")
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun showNotification(builder: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
//            notify(88, builder.build())
            startForeground(88, builder.build())
        }
    }

    private fun createNotification() {
        val channelID = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) "CHANNEL_ID" else null
        val builder = if (channelID.isNullOrEmpty()) NotificationCompat.Builder(this) else NotificationCompat.Builder(this, channelID)
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