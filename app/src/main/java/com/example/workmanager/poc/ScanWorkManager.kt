package com.example.workmanager.poc

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat.startForegroundService
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.Calendar
import java.util.concurrent.TimeUnit

class ScanWorkManager(var appContext: Context, params: WorkerParameters) :
    Worker(appContext, params) {

    private var shouldStopService = true

    override fun doWork(): Result {
        val currentTime = Calendar.getInstance()
        val timePeriods = AppPrefHelper().getTimePeriodsList()
        timePeriods?.forEach { timePeriod ->
            if (currentTime[Calendar.HOUR_OF_DAY] >= timePeriod.start[Calendar.HOUR_OF_DAY]
                && currentTime[Calendar.HOUR_OF_DAY] <= timePeriod.end[Calendar.HOUR_OF_DAY]
            ) {
                if ((currentTime[Calendar.HOUR_OF_DAY] == timePeriod.start[Calendar.HOUR_OF_DAY]
                    && currentTime[Calendar.MINUTE] > timePeriod.start[Calendar.MINUTE])
                    || (currentTime[Calendar.HOUR_OF_DAY] == timePeriod.end[Calendar.HOUR_OF_DAY]
                    && currentTime[Calendar.MINUTE] < timePeriod.end[Calendar.MINUTE])){
                    println(currentTime)
                    shouldStopService = false
                    Intent(appContext, DummyService::class.java).also { intent ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) appContext.startForegroundService(intent) else appContext.startService(intent)
                    }
                }
            }
        }
        if (shouldStopService){
            Intent(appContext, DummyService::class.java).also { intent ->
                appContext.stopService(intent)
            }
        }
        return Result.success()
    }

    companion object {
        const val PERIODIC_WORK_MANAGER = "PERIODIC_WORK_MANAGER"
        fun startPeriodicWorkManager() {
            val periodicRefreshRequest = PeriodicWorkRequest.Builder(
                ScanWorkManager::class.java, // Your worker class
                15, // repeating interval
                TimeUnit.MINUTES
            )

            val periodicWorkRequest: PeriodicWorkRequest = periodicRefreshRequest
                .build()
            WorkManager.getInstance(App.getApplication()).enqueueUniquePeriodicWork(
                PERIODIC_WORK_MANAGER,
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWorkRequest
            )
        }
    }
}