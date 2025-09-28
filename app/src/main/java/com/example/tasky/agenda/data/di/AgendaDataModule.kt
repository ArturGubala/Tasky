package com.example.tasky.agenda.data.di

import com.example.tasky.agenda.data.network.event.KtorEventDataSource
import com.example.tasky.agenda.data.network.reminder.KtorReminderDataSource
import com.example.tasky.agenda.data.network.task.KtorTaskDataSource
import com.example.tasky.agenda.data.network.tasky.KtorTaskyDataSource
import com.example.tasky.agenda.data.repository.OfflineFirstEventRepository
import com.example.tasky.agenda.data.repository.OfflineFirstReminderRepository
import com.example.tasky.agenda.data.repository.OfflineFirstTaskRepository
import com.example.tasky.agenda.data.repository.TaskyRepositoryImpl
import com.example.tasky.agenda.data.sync.DeleteAgendaItemWorker
import com.example.tasky.agenda.data.sync.FetchAgendaItemsWorker
import com.example.tasky.agenda.data.sync.SyncAgendaItemWorkerScheduler
import com.example.tasky.agenda.data.sync.UpsertAgendaItemWorker
import com.example.tasky.agenda.domain.data.EventRepository
import com.example.tasky.agenda.domain.data.ReminderRepository
import com.example.tasky.agenda.domain.data.TaskRepository
import com.example.tasky.agenda.domain.data.TaskyRepository
import com.example.tasky.agenda.domain.data.network.EventRemoteDataSource
import com.example.tasky.agenda.domain.data.network.ReminderRemoteDataSource
import com.example.tasky.agenda.domain.data.network.TaskRemoteDataSource
import com.example.tasky.agenda.domain.data.network.TaskyRemoteDataSource
import com.example.tasky.agenda.domain.data.sync.SyncAgendaItemScheduler
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val agendaNetworkModule = module {
    singleOf(::KtorTaskDataSource).bind<TaskRemoteDataSource>()
    singleOf(::KtorReminderDataSource).bind<ReminderRemoteDataSource>()
    singleOf(::KtorEventDataSource).bind<EventRemoteDataSource>()
    singleOf(::KtorTaskyDataSource).bind<TaskyRemoteDataSource>()

    singleOf(::OfflineFirstTaskRepository).bind<TaskRepository>()
    singleOf(::OfflineFirstReminderRepository).bind<ReminderRepository>()
    singleOf(::OfflineFirstEventRepository).bind<EventRepository>()
    singleOf(::TaskyRepositoryImpl).bind<TaskyRepository>()

    workerOf(::UpsertAgendaItemWorker)
    workerOf(::DeleteAgendaItemWorker)
    workerOf(::FetchAgendaItemsWorker)

    singleOf(::SyncAgendaItemWorkerScheduler).bind<SyncAgendaItemScheduler>()
}
