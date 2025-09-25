package com.example.tasky.agenda.data.sync.task

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tasky.agenda.data.sync.toWorkerResult
import com.example.tasky.agenda.domain.data.network.TaskRemoteDataSource
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.data.database.task.dao.TaskPendingSyncDao
import com.example.tasky.core.data.database.task.mappers.toTask
import com.example.tasky.core.domain.util.Result.Error
import com.example.tasky.core.domain.util.Result.Success


class UpsertTaskWorker(
    context: Context,
    private val params: WorkerParameters,
    private val taskRemoteDataSource: TaskRemoteDataSource,
    private val pendingSyncDao: TaskPendingSyncDao,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        val pendingTaskId = params.inputData.getString(TASK_ID) ?: return Result.failure()
        val pendingTaskEntity = pendingSyncDao.getTaskPendingSyncEntity(pendingTaskId)
            ?: return Result.failure()

        val task = pendingTaskEntity.task.toTask()

        when (pendingTaskEntity.operation) {
            SyncOperation.CREATE -> {
                return when (val result = taskRemoteDataSource.createTask(task)) {
                    is Error -> {
                        result.error.toWorkerResult()
                    }

                    is Success -> {
                        pendingSyncDao.deleteTaskPendingSyncEntity(pendingTaskId)
                        Result.success()
                    }
                }
            }

            SyncOperation.UPDATE -> {
                return when (val result = taskRemoteDataSource.updateTask(task)) {
                    is Error -> {
                        result.error.toWorkerResult()
                    }

                    is Success -> {
                        pendingSyncDao.deleteTaskPendingSyncEntity(pendingTaskId)
                        Result.success()
                    }
                }
            }
        }
    }

    companion object Companion {
        const val TASK_ID = "TASK_ID"
    }
}
