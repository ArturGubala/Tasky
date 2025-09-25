package com.example.tasky.agenda.data.sync.task

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.example.tasky.agenda.data.sync.UpsertAgendaItemWorker
import com.example.tasky.agenda.domain.data.sync.SyncAgendaItemScheduler
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.agenda.domain.util.AgendaItemType
import com.example.tasky.agenda.domain.util.toInt
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

class SyncAgendaItemWorkerScheduler(
    private val context: Context,
    private val pendingSyncDao: TaskPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope,
) : SyncAgendaItemScheduler {

    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(type: SyncAgendaItemScheduler.SyncType) {
        when (type) {
            is SyncAgendaItemScheduler.SyncType.FetchAgendaItem -> {}
            is SyncAgendaItemScheduler.SyncType.DeleteAgendaItem -> scheduleDeleteAgendaItemWorker(
                type.taskId
            )

            is SyncAgendaItemScheduler.SyncType.UpsertAgendaItem -> scheduleUpsertAgendaItemWorker(
                item = type.task,
                operation = type.operation,
                itemType = type.itemType
            )
        }
    }

    private suspend fun scheduleDeleteAgendaItemWorker(taskId: String) {
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

    private suspend fun scheduleUpsertAgendaItemWorker(
        item: Any, operation: SyncOperation,
        itemType: AgendaItemType,
    ) {
        val userId = sessionStorage.get()?.userId ?: return
        var itemId: String

        when (itemType) {
            AgendaItemType.TASK -> {
                val task = item as Task
                itemId = task.id
                val pendingTask = TaskPendingSyncEntity(
                    task = task.toTaskEntity(),
                    operation = operation,
                    userId = userId
                )
                pendingSyncDao.upsertTaskPendingSyncEntity(pendingTask)
            }

            AgendaItemType.EVENT -> {
                val event = item as Event
                itemId = event.id
            }

            AgendaItemType.REMINDER -> {
                val reminder = item as Reminder
                itemId = reminder.id
            }
        }


        val workRequest = OneTimeWorkRequestBuilder<UpsertAgendaItemWorker>()
            .addTag("upsert_work")
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
                    .putString(UpsertAgendaItemWorker.ITEM_ID, itemId)
                    .putInt(UpsertAgendaItemWorker.ITEM_TYPE, itemType.toInt())
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
