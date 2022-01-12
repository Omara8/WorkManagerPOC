package com.example.workmanager.poc

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val addNew: MaterialButton = findViewById(R.id.add_new)
        val clearPref: MaterialButton = findViewById(R.id.clear_all)
        val timePeriodsLayout: LinearLayout = findViewById(R.id.time_periods_linear_layout)
        val firstTimePeriod: TimePeriodLayout = findViewById(R.id.first_time_period)
        val geofenceSetting: MaterialButton = findViewById(R.id.go_geofence)

        geofenceSetting.setOnClickListener {
            startActivity(Intent(this, GeoFenceActivity::class.java))
        }
        clearPref.setOnClickListener {
            AppPrefHelper().clearPreferences()
            timePeriodsLayout.removeAllViews()
            timePeriodsLayout.addView(TimePeriodLayout(this, attributeSet = null))
        }
        addNew.setOnClickListener {
            timePeriodsLayout.addView(TimePeriodLayout(this, attributeSet = null))
        }

        createNotificationChannel()

        var timePeriodList = AppPrefHelper().getTimePeriodsList() ?: mutableListOf()

        if (!timePeriodList.isNullOrEmpty()) {
            firstTimePeriod.visibility = View.GONE
        }
        timePeriodList.forEach { timePeriod ->
            val rowLayout = TimePeriodLayout(this, attributeSet = null)
            rowLayout.setTimePeriods(timePeriod)
            timePeriodsLayout.addView(rowLayout)
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "channel_name"
            val descriptionText = "channel_description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel("CHANNEL_ID", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}