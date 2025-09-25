package com.example.tasky.agenda.data.sync.task

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tasky.agenda.data.sync.toWorkerResult
import com.example.tasky.agenda.domain.data.network.TaskRemoteDataSource
import com.example.tasky.core.data.database.task.dao.TaskPendingSyncDao
import com.example.tasky.core.domain.util.Result.Error
import com.example.tasky.core.domain.util.Result.Success

class DeleteTaskWorker(
    context: Context,
    private val params: WorkerParameters,
    private val taskRemoteDataSource: TaskRemoteDataSource,
    private val pendingSyncDao: TaskPendingSyncDao,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }
        val taskId = params.inputData.getString(TASK_ID) ?: return Result.failure()
        return when (val result = taskRemoteDataSource.deleteTask(taskId)) {
            is Error -> {
                result.error.toWorkerResult()
            }

            is Success -> {
                pendingSyncDao.deleteDeletedTaskSyncEntity(taskId)
                Result.success()
            }
        }
    }

    companion object Companion {
        const val TASK_ID = "TASK_ID"
    }
}
