package com.example.workmanager.poc

import android.app.Application

class App: Application() {

    companion object {
        private var instance: App? = null

        fun getApplication(): App {
            return instance ?: App()
        }
    }

    override fun onCreate() {
        super.onCreate()
        AppPrefHelper.initPreferences(this)
        ScanWorkManager.startPeriodicWorkManager()
    }

}