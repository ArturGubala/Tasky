package com.example.tasky.agenda.data.di

import com.example.tasky.agenda.data.network.task.KtorTaskDataSource
import com.example.tasky.agenda.data.repository.OfflineFirstTaskRepository
import com.example.tasky.agenda.domain.data.TaskRepository
import com.example.tasky.agenda.domain.data.network.TaskRemoteDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val agendaNetworkModule = module {
    singleOf(::KtorTaskDataSource).bind<TaskRemoteDataSource>()
    singleOf(::OfflineFirstTaskRepository).bind<TaskRepository>()
}
