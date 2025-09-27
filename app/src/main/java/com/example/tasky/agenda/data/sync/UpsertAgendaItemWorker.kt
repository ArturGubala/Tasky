package com.example.tasky.agenda.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tasky.agenda.domain.data.network.ReminderRemoteDataSource
import com.example.tasky.agenda.domain.data.network.TaskRemoteDataSource
import com.example.tasky.agenda.domain.util.AgendaKind
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.data.database.reminder.dao.ReminderPendingSyncDao
import com.example.tasky.core.data.database.reminder.mappers.toReminder
import com.example.tasky.core.data.database.task.dao.TaskPendingSyncDao
import com.example.tasky.core.data.database.task.mappers.toTask
import com.example.tasky.core.domain.util.Result.Error as ResultError
import com.example.tasky.core.domain.util.Result.Success as ResultSuccess

class UpsertAgendaItemWorker(
    context: Context,
    params: WorkerParameters,
    private val taskRemoteDataSource: TaskRemoteDataSource,
    private val taskPendingSyncDao: TaskPendingSyncDao,
    private val reminderRemoteDataSource: ReminderRemoteDataSource,
    private val reminderPendingSyncDao: ReminderPendingSyncDao,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        val id = inputData.getString(ITEM_ID) ?: return Result.failure()
        val kind = inputData.getString(ITEM_KIND)
            ?.let { runCatching { AgendaKind.valueOf(it) }.getOrNull() }
            ?: return Result.failure()

        return when (kind) {
            AgendaKind.TASK -> {
                upsertTask(itemId = id)
            }

            AgendaKind.EVENT -> {
                upsertEvent(itemId = id)
            }

            AgendaKind.REMINDER -> {
                upsertReminder(itemId = id)
            }
        }
    }

    private suspend fun upsertTask(itemId: String): Result {
        val pendingTaskEntity = taskPendingSyncDao.getTaskPendingSyncEntity(itemId)
            ?: return Result.failure()

        when (pendingTaskEntity.operation) {
            SyncOperation.CREATE -> {
                val pendingUpdateEntity =
                    taskPendingSyncDao.getTaskPendingSyncEntityByIdAndOperation(
                    taskId = itemId,
                    operation = SyncOperation.UPDATE
                )

                val task = if (pendingUpdateEntity != null) {
                    taskPendingSyncDao.deleteTaskPendingSyncEntity(
                        taskId = pendingUpdateEntity.taskId,
                        operations = listOf(SyncOperation.UPDATE)
                    )
                    pendingUpdateEntity.task.toTask()
                } else {
                    pendingTaskEntity.task.toTask()
                }

                return when (val result = taskRemoteDataSource.createTask(task)) {
                    is ResultError -> {
                        result.error.toWorkerResult()
                    }

                    is ResultSuccess -> {
                        taskPendingSyncDao.deleteTaskPendingSyncEntity(
                            taskId = itemId,
                            operations = listOf(SyncOperation.CREATE)
                        )
                        Result.success()
                    }
                }
            }

            SyncOperation.UPDATE -> {
                val pendingCreate = taskPendingSyncDao.getTaskPendingSyncEntityByIdAndOperation(
                    taskId = itemId,
                    operation = SyncOperation.CREATE
                )

                if (pendingCreate != null) {
                    val updatedCreateEntity = pendingCreate.copy(
                        task = pendingTaskEntity.task,
                        operation = SyncOperation.CREATE
                    )
                    taskPendingSyncDao.upsertTaskPendingSyncEntity(updatedCreateEntity)
                    taskPendingSyncDao.deleteTaskPendingSyncEntity(
                        taskId = itemId,
                        operations = listOf(SyncOperation.UPDATE)
                    )

                    return Result.success()
                }

                val task = pendingTaskEntity.task.toTask()
                return when (val result = taskRemoteDataSource.updateTask(task)) {
                    is ResultError -> {
                        result.error.toWorkerResult()
                    }

                    is ResultSuccess -> {
                        taskPendingSyncDao.deleteTaskPendingSyncEntity(
                            taskId = itemId,
                            operations = listOf(SyncOperation.UPDATE)
                        )
                        Result.success()
                    }
                }
            }
        }
    }

    private suspend fun upsertEvent(itemId: String): Result {

        return Result.success()
    }

    private suspend fun upsertReminder(itemId: String): Result {
        val pendingReminderEntity = reminderPendingSyncDao.getReminderPendingSyncEntity(itemId)
            ?: return Result.failure()

        when (pendingReminderEntity.operation) {
            SyncOperation.CREATE -> {
                val pendingUpdateEntity =
                    reminderPendingSyncDao.getReminderPendingSyncEntityByIdAndOperation(
                        reminderId = itemId,
                        operation = SyncOperation.UPDATE
                    )

                val reminder = if (pendingUpdateEntity != null) {
                    reminderPendingSyncDao.deleteReminderPendingSyncEntity(
                        reminderId = pendingUpdateEntity.reminderId,
                        operations = listOf(SyncOperation.UPDATE)
                    )
                    pendingUpdateEntity.reminder.toReminder()
                } else {
                    pendingReminderEntity.reminder.toReminder()
                }

                return when (val result = reminderRemoteDataSource.createReminder(reminder)) {
                    is ResultError -> {
                        result.error.toWorkerResult()
                    }

                    is ResultSuccess -> {
                        reminderPendingSyncDao.deleteReminderPendingSyncEntity(
                            reminderId = itemId,
                            operations = listOf(SyncOperation.CREATE)
                        )
                        Result.success()
                    }
                }
            }

            SyncOperation.UPDATE -> {
                val pendingCreateEntity =
                    reminderPendingSyncDao.getReminderPendingSyncEntityByIdAndOperation(
                        reminderId = itemId,
                        operation = SyncOperation.CREATE
                    )

                if (pendingCreateEntity != null) {
                    val updatedCreateEntity = pendingCreateEntity.copy(
                        reminder = pendingReminderEntity.reminder,
                        operation = SyncOperation.CREATE
                    )
                    reminderPendingSyncDao.upsertReminderPendingSyncEntity(updatedCreateEntity)
                    reminderPendingSyncDao.deleteReminderPendingSyncEntity(
                        reminderId = itemId,
                        operations = listOf(SyncOperation.UPDATE)
                    )

                    return Result.success()
                }

                val reminder = pendingReminderEntity.reminder.toReminder()
                return when (val result = reminderRemoteDataSource.updateReminder(reminder)) {
                    is ResultError -> {
                        result.error.toWorkerResult()
                    }

                    is ResultSuccess -> {
                        reminderPendingSyncDao.deleteReminderPendingSyncEntity(
                            reminderId = itemId,
                            operations = listOf(SyncOperation.UPDATE)
                        )
                        Result.success()
                    }
                }
            }
        }
    }

    companion object Companion {
        const val ITEM_ID = "ITEM_ID"
        const val ITEM_KIND = "ITEM_KIND"
    }
}
