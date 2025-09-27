package com.example.tasky.core.data.database.di

import androidx.room.Room
import com.example.tasky.core.data.database.TaskyDatabase
import com.example.tasky.core.data.database.event.RoomLocalEventDataSource
import com.example.tasky.core.data.database.reminder.RoomLocalReminderDataSource
import com.example.tasky.core.data.database.task.RoomLocalTaskDataSource
import com.example.tasky.core.domain.data.EventLocalDataSource
import com.example.tasky.core.domain.data.ReminderLocalDataSource
import com.example.tasky.core.domain.data.TaskLocalDataSource
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
    single { get<TaskyDatabase>().taskPendingSyncDao }
    single { get<TaskyDatabase>().reminderDao }
    single { get<TaskyDatabase>().reminderPendingSyncDao }
    single { get<TaskyDatabase>().eventDao }
    single { get<TaskyDatabase>().eventPendingSyncDao }

    singleOf(::RoomLocalTaskDataSource).bind<TaskLocalDataSource>()
    singleOf(::RoomLocalReminderDataSource).bind<ReminderLocalDataSource>()
    singleOf(::RoomLocalEventDataSource).bind<EventLocalDataSource>()
}
