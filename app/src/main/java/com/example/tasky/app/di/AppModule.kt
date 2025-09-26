package com.example.tasky.app.di

import com.example.tasky.app.MainViewModel
import com.example.tasky.app.TaskyApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)
    single<CoroutineScope> {
        (androidApplication() as TaskyApp).applicationScope
    }
}
