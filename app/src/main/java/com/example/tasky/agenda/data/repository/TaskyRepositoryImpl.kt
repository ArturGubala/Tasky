package com.example.tasky.agenda.data.repository

import com.example.tasky.agenda.domain.data.TaskyRepository
import com.example.tasky.agenda.domain.data.network.TaskyRemoteDataSource
import com.example.tasky.core.domain.data.ReminderLocalDataSource
import com.example.tasky.core.domain.data.TaskLocalDataSource
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.asEmptyDataResult
import com.example.tasky.core.domain.util.onSuccess

class TaskyRepositoryImpl(
    private val taskyRemoteDataSource: TaskyRemoteDataSource,
    private val taskLocalDataSource: TaskLocalDataSource,
    private val reminderLocalDataSource: ReminderLocalDataSource,
) : TaskyRepository {

    override suspend fun fetchFullAgenda(): EmptyResult<DataError> {
        return taskyRemoteDataSource.getFullAgenda()
            .onSuccess { result ->
                taskLocalDataSource.insertTasks(result.tasks)
                reminderLocalDataSource.insertReminders(result.reminders)
            }.asEmptyDataResult()
    }
}
