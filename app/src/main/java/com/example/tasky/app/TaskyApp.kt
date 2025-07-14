package com.example.tasky.app

import android.app.Application
import com.example.tasky.BuildConfig
import timber.log.Timber

class TaskyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
