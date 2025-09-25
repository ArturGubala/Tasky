package com.example.tasky.agenda.data.sync.task

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.example.tasky.agenda.domain.data.sync.SyncTaskScheduler
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.data.database.task.dao.TaskPendingSyncDao
import com.example.tasky.core.data.database.task.entity.TaskDeletedSyncEntity
import com.example.tasky.core.data.database.task.entity.TaskPendingSyncEntity
import com.example.tasky.core.data.database.task.mappers.toTaskEntity
import com.example.tasky.core.domain.datastore.SessionStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

class SyncTaskWorkerScheduler(
    private val context: Context,
    private val pendingSyncDao: TaskPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope,
) : SyncTaskScheduler {

    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(type: SyncTaskScheduler.SyncType) {
        when (type) {
            is SyncTaskScheduler.SyncType.FetchTasks -> {}
            is SyncTaskScheduler.SyncType.DeleteTask -> scheduleDeleteTaskWorker(type.taskId)
            is SyncTaskScheduler.SyncType.UpsertTask -> scheduleUpsertTaskWorker(
                task = type.task,
                operation = type.operation
            )
        }
    }

    private suspend fun scheduleDeleteTaskWorker(taskId: String) {
        val userId = sessionStorage.get()?.userId ?: return
        val entity = TaskDeletedSyncEntity(
            taskId = taskId,
            userId = userId
        )
        pendingSyncDao.upsertDeletedTaskSyncEntity(entity)

        val workRequest = OneTimeWorkRequestBuilder<DeleteTaskWorker>()
            .addTag("delete_work")
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(DeleteTaskWorker.TASK_ID, entity.taskId)
                    .build()
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleUpsertTaskWorker(task: Task, operation: SyncOperation) {
        val userId = sessionStorage.get()?.userId ?: return

        val pendingTask = TaskPendingSyncEntity(
            task = task.toTaskEntity(),
            operation = operation,
            userId = userId
        )
        pendingSyncDao.upsertTaskPendingSyncEntity(pendingTask)

        val workRequest = OneTimeWorkRequestBuilder<UpsertTaskWorker>()
            .addTag("create_work")
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(UpsertTaskWorker.TASK_ID, pendingTask.taskId)
                    .build()
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleFetchRunsWorker(interval: Duration) {
        // TODO just for test
        return
//        val isSyncScheduled = withContext(Dispatchers.IO) {
//            workManager
//                .getWorkInfosByTag("sync_work")
//                .get()
//                .isNotEmpty()
//        }
//        if(isSyncScheduled) {
//            return
//        }
//
//        val workRequest = PeriodicWorkRequestBuilder<>(
//            repeatInterval = interval.toJavaDuration()
//        )
//            .setConstraints(
//                Constraints.Builder()
//                    .setRequiredNetworkType(NetworkType.CONNECTED)
//                    .build()
//            )
//            .setBackoffCriteria(
//                backoffPolicy = BackoffPolicy.EXPONENTIAL,
//                backoffDelay = 2000L,
//                timeUnit = TimeUnit.MILLISECONDS
//            )
//            .setInitialDelay(
//                duration = 30,
//                timeUnit = TimeUnit.MINUTES
//            )
//            .addTag("sync_work")
//            .build()
//
//        workManager.enqueue(workRequest).await()
    }

    override suspend fun cancelAllSyncs() {
        WorkManager.getInstance(context)
            .cancelAllWork()
            .await()
    }
}
