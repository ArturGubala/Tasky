package com.example.tasky.agenda.data.repository

import com.example.tasky.agenda.domain.data.ReminderRepository
import com.example.tasky.agenda.domain.data.network.ReminderRemoteDataSource
import com.example.tasky.agenda.domain.data.sync.SyncAgendaItemScheduler
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.notification.NotificationScheduler
import com.example.tasky.agenda.domain.notification.toNotification
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.data.database.reminder.dao.ReminderPendingSyncDao
import com.example.tasky.core.data.database.reminder.mappers.toReminder
import com.example.tasky.core.domain.data.ReminderLocalDataSource
import com.example.tasky.core.domain.datastore.SessionStorage
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.domain.util.asEmptyDataResult
import com.example.tasky.core.domain.util.onError
import com.example.tasky.core.domain.util.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfflineFirstReminderRepository(
    private val reminderRemoteDataSource: ReminderRemoteDataSource,
    private val reminderLocalDataSource: ReminderLocalDataSource,
    private val applicationScope: CoroutineScope,
    private val reminderPendingSyncDao: ReminderPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val syncAgendaItemScheduler: SyncAgendaItemScheduler,
    private val notificationScheduler: NotificationScheduler,
) : ReminderRepository {

    override suspend fun upsertReminder(
        reminder: Reminder,
        syncOperation: SyncOperation,
    ): EmptyResult<DataError> {
        val localResult = reminderLocalDataSource.upsertReminder(reminder)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }
        notificationScheduler.scheduleNotification(reminder.toNotification())

        when (syncOperation) {
            SyncOperation.CREATE -> {
                return reminderRemoteDataSource.createReminder(reminder)
                    .onSuccess { reminder ->
                        notificationScheduler.scheduleNotification(reminder.toNotification())
                    }
                    .onError { error ->
                        applicationScope.launch {
                            syncAgendaItemScheduler.scheduleSync(
                                type = SyncAgendaItemScheduler.SyncType.UpsertAgendaItem(
                                    item = AgendaItem.Reminder(id = reminder.id),
                                    operation = syncOperation
                                )
                            )
                        }.join()
                    }.asEmptyDataResult()
            }

            SyncOperation.UPDATE -> {
                return reminderRemoteDataSource.updateReminder(reminder)
                    .onSuccess { reminder ->
                        notificationScheduler.scheduleNotification(reminder.toNotification())
                    }
                    .onError { error ->
                        applicationScope.launch {
                            syncAgendaItemScheduler.scheduleSync(
                                type = SyncAgendaItemScheduler.SyncType.UpsertAgendaItem(
                                    item = AgendaItem.Reminder(id = reminder.id),
                                    operation = syncOperation
                                )
                            )
                        }.join()
                    }.asEmptyDataResult()
            }
        }
    }

    override suspend fun deleteReminder(id: String): EmptyResult<DataError> {
        reminderLocalDataSource.deleteReminder(id = id)
        notificationScheduler.cancelNotification(id)

        return reminderRemoteDataSource.deleteReminder(id = id)
            .onError { error ->
                applicationScope.launch {
                    syncAgendaItemScheduler.scheduleSync(
                        type = SyncAgendaItemScheduler.SyncType.DeleteAgendaItem(
                            item = AgendaItem.Reminder(id = id)
                        )
                    )
                }.join()
            }.asEmptyDataResult()
    }

    override suspend fun syncPendingReminders() {
        withContext(Dispatchers.IO) {
            val userId = sessionStorage.get()?.userId ?: return@withContext

            val createdReminders = async {
                reminderPendingSyncDao.getAllReminderPendingSyncEntities(userId)
            }
            val deletedReminders = async {
                reminderPendingSyncDao.getAllDeletedReminderSyncEntities(userId)
            }

            val createJobs = createdReminders
                .await()
                .map { it ->
                    launch {
                        val reminder = it.reminder.toReminder()
                        when (it.operation) {
                            SyncOperation.CREATE -> {
                                reminderRemoteDataSource.createReminder(reminder)
                                    .onSuccess {
                                        notificationScheduler.scheduleNotification(reminder.toNotification())
                                        applicationScope.launch {
                                            reminderPendingSyncDao.deleteReminderPendingSyncEntity(
                                                reminderId = it.id,
                                                operations = listOf(SyncOperation.CREATE)
                                            )
                                        }.join()
                                    }
                            }

                            SyncOperation.UPDATE -> {
                                reminderRemoteDataSource.updateReminder(reminder)
                                    .onSuccess {
                                        notificationScheduler.scheduleNotification(reminder.toNotification())
                                        applicationScope.launch {
                                            reminderPendingSyncDao.deleteReminderPendingSyncEntity(
                                                reminderId = it.id,
                                                operations = listOf(SyncOperation.UPDATE)
                                            )
                                        }.join()
                                    }
                            }
                        }
                    }
                }
            val deleteJobs = deletedReminders
                .await()
                .map {
                    launch {
                        reminderRemoteDataSource.deleteReminder(it.reminderId)
                            .onSuccess { result ->
                                applicationScope.launch {
                                    reminderPendingSyncDao.deleteDeletedReminderSyncEntity(it.reminderId)
                                }.join()
                            }
                    }
                }

            createJobs.forEach { it.join() }
            deleteJobs.forEach { it.join() }
        }
    }

}
