package com.example.tasky.core.data.database.di

import androidx.room.Room
import com.example.tasky.core.data.database.TaskyDatabase
import com.example.tasky.core.data.database.task.RoomLocalTaskDataSource
import com.example.tasky.core.domain.data.TaskLocalDataStore
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            TaskyDatabase::class.java,
            "tasky.db"
        ).build()
    }
    single { get<TaskyDatabase>().taskDao }
    singleOf(::RoomLocalTaskDataSource).bind<TaskLocalDataStore>()
}
