package com.example.workmanager.poc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(p1)
        val geofenceTransition = geofencingEvent.geofenceTransition
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            Intent(p0, DummyService::class.java).also { intent ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) p0?.startForegroundService(intent) else p0?.startService(intent)
            }
            Toast.makeText(p0,"Entered geofence", Toast.LENGTH_LONG).show()
        }else{
            Intent(p0, DummyService::class.java).also { intent ->
                p0?.stopService(intent)
            }
            Toast.makeText(p0,"Left geofence", Toast.LENGTH_LONG).show()
        }
    }
}