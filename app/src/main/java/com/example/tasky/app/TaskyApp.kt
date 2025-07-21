package com.example.tasky.app

import android.app.Application
import com.example.tasky.BuildConfig
import com.example.tasky.auth.di.authDataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class TaskyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@TaskyApp)
            modules(
                modules =
                    authDataModule
            )
        }
    }
}
