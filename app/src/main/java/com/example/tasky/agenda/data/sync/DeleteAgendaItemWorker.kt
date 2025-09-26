package com.example.tasky.agenda.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tasky.agenda.domain.data.network.TaskRemoteDataSource
import com.example.tasky.agenda.domain.util.AgendaKind
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.data.database.task.dao.TaskPendingSyncDao
import com.example.tasky.core.domain.util.Result.Error as ResultError
import com.example.tasky.core.domain.util.Result.Success as ResultSuccess

class DeleteAgendaItemWorker(
    context: Context,
    params: WorkerParameters,
    private val taskRemoteDataSource: TaskRemoteDataSource,
    private val taskPendingSyncDao: TaskPendingSyncDao,
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
                deleteTask(itemId = id)
            }

            AgendaKind.EVENT -> {
                deleteEvent(itemId = id)
            }

            AgendaKind.REMINDER -> {
                deleteReminder(itemId = id)
            }
        }
    }

    private suspend fun deleteTask(itemId: String): Result {
        val pendingCreateTask = taskPendingSyncDao.getTaskPendingSyncEntityByIdAndOperation(
            taskId = itemId, operation = SyncOperation.CREATE
        )
        val pendingUpdateTask = taskPendingSyncDao.getTaskPendingSyncEntityByIdAndOperation(
            taskId = itemId, operation = SyncOperation.CREATE
        )

        if (pendingCreateTask != null && pendingUpdateTask != null) {
            taskPendingSyncDao.deleteTaskPendingSyncEntity(
                taskId = itemId,
                operations = listOf(SyncOperation.CREATE, SyncOperation.UPDATE)
            )
            taskPendingSyncDao.deleteDeletedTaskSyncEntity(taskId = itemId)

            return Result.success()
        } else if (pendingCreateTask != null) {
            taskPendingSyncDao.deleteTaskPendingSyncEntity(
                taskId = itemId,
                operations = listOf(SyncOperation.CREATE)
            )
            taskPendingSyncDao.deleteDeletedTaskSyncEntity(taskId = itemId)

            return Result.success()
        } else if (pendingUpdateTask != null) {
            taskPendingSyncDao.deleteTaskPendingSyncEntity(
                taskId = itemId,
                operations = listOf(SyncOperation.CREATE)
            )
        }

        return when (val result = taskRemoteDataSource.deleteTask(id = itemId)) {
            is ResultError -> {
                result.error.toWorkerResult()
            }

            is ResultSuccess -> {
                taskPendingSyncDao.deleteDeletedTaskSyncEntity(taskId = itemId)
                Result.success()
            }
        }
    }

    private suspend fun deleteEvent(itemId: String): Result {
        return Result.success()
    }

    private suspend fun deleteReminder(itemId: String): Result {
        return Result.success()
    }

    companion object Companion {
        const val ITEM_ID = "ITEM_ID"
        const val ITEM_KIND = "ITEM_KIND"
    }
}
