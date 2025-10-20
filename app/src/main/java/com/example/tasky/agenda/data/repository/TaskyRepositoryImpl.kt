package com.example.tasky.agenda.data.repository

import com.example.tasky.agenda.domain.data.TaskyRepository
import com.example.tasky.agenda.domain.data.network.TaskyRemoteDataSource
import com.example.tasky.agenda.domain.data.sync.SyncAgendaItemScheduler
import com.example.tasky.agenda.domain.notification.NotificationScheduler
import com.example.tasky.core.domain.data.EventLocalDataSource
import com.example.tasky.core.domain.data.ReminderLocalDataSource
import com.example.tasky.core.domain.data.TaskLocalDataSource
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.asEmptyDataResult
import com.example.tasky.core.domain.util.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TaskyRepositoryImpl(
    private val taskyRemoteDataSource: TaskyRemoteDataSource,
    private val taskLocalDataSource: TaskLocalDataSource,
    private val reminderLocalDataSource: ReminderLocalDataSource,
    private val eventLocalDataSource: EventLocalDataSource,
    private val applicationScope: CoroutineScope,
    private val syncAgendaItemScheduler: SyncAgendaItemScheduler,
    private val notificationScheduler: NotificationScheduler,
) : TaskyRepository {

    override suspend fun fetchFullAgenda(): EmptyResult<DataError> {
        return taskyRemoteDataSource.getFullAgenda()
            .onSuccess { result ->
                taskLocalDataSource.insertTasks(result.tasks)
                reminderLocalDataSource.insertReminders(result.reminders)
                eventLocalDataSource.insertEvents(result.events)
            }.asEmptyDataResult()
    }

    override suspend fun cleanUpLocalData() {
        applicationScope.launch {
            val taskIds = taskLocalDataSource.getTasksIds().first()
            val reminderIds = reminderLocalDataSource.getRemindersIds().first()
            val eventIds = eventLocalDataSource.getEventsIds().first()
            val allIds = taskIds + reminderIds + eventIds

            taskLocalDataSource.deleteTasks()
            reminderLocalDataSource.deleteReminders()
            eventLocalDataSource.deleteEvents()
            syncAgendaItemScheduler.cancelAllSyncs()
            notificationScheduler.cancelNotifications(allIds)
        }
    }
}
