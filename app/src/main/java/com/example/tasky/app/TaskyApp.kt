package com.example.tasky.app

import android.app.Application
import com.example.tasky.BuildConfig
import com.example.tasky.agenda.data.di.agendaNetworkModule
import com.example.tasky.agenda.di.agendaViewModelModule
import com.example.tasky.app.di.appModule
import com.example.tasky.auth.di.authDataModule
import com.example.tasky.auth.presentation.di.authViewModelModule
import com.example.tasky.core.data.di.coreDataModule
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
                appModule,
                authDataModule,
                authViewModelModule,
                coreDataModule,
                agendaViewModelModule,
                agendaNetworkModule
            )
        }
    }
}
