package com.example.tasky.agenda.data.sync

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import androidx.work.workDataOf
import com.example.tasky.agenda.domain.data.sync.SyncAgendaItemScheduler
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.kind
import com.example.tasky.agenda.domain.util.AgendaKind
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.data.database.task.dao.TaskPendingSyncDao
import com.example.tasky.core.data.database.task.entity.TaskDeletedSyncEntity
import com.example.tasky.core.data.database.task.entity.TaskPendingSyncEntity
import com.example.tasky.core.data.database.task.mappers.toTaskEntity
import com.example.tasky.core.domain.data.TaskLocalDataStore
import com.example.tasky.core.domain.datastore.SessionStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

class SyncAgendaItemWorkerScheduler(
    private val context: Context,
    private val taskLocalDataStore: TaskLocalDataStore,
    private val taskPendingSyncDao: TaskPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope,
) : SyncAgendaItemScheduler {

    private val workManager = WorkManager.Companion.getInstance(context)

    override suspend fun scheduleSync(type: SyncAgendaItemScheduler.SyncType) {
        when (type) {
            is SyncAgendaItemScheduler.SyncType.FetchAgendaItem -> {}
            is SyncAgendaItemScheduler.SyncType.DeleteAgendaItem ->
                scheduleDelete(item = type.item)
            is SyncAgendaItemScheduler.SyncType.UpsertAgendaItem ->
                scheduleUpsert(item = type.item, operation = type.operation)
        }
    }

    private suspend fun scheduleDelete(item: AgendaItem) {
        val userId = sessionStorage.get()?.userId ?: return
        val kind = item.kind()
        val data = workDataOf(
            DeleteAgendaItemWorker.ITEM_ID to item.id,
            DeleteAgendaItemWorker.ITEM_KIND to kind.name
        )

        when (kind) {
            AgendaKind.TASK -> {
                val pendingTask = TaskDeletedSyncEntity(
                    taskId = item.id,
                    userId = userId
                )
                taskPendingSyncDao.upsertDeletedTaskSyncEntity(pendingTask)
            }

            AgendaKind.EVENT -> {

            }

            AgendaKind.REMINDER -> {

            }
        }

        val workRequest = OneTimeWorkRequestBuilder<DeleteAgendaItemWorker>()
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
            .setInputData(data)
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleUpsert(item: AgendaItem, operation: SyncOperation) {
        val userId = sessionStorage.get()?.userId ?: return
        val kind = item.kind()
        val data = workDataOf(
            UpsertAgendaItemWorker.ITEM_ID to item.id,
            UpsertAgendaItemWorker.ITEM_KIND to kind.name
        )

        when (kind) {
            AgendaKind.TASK -> {
                val task = taskLocalDataStore.getTask(id = item.id).firstOrNull() ?: return
                val pendingTask = TaskPendingSyncEntity(
                    task = task.toTaskEntity(),
                    operation = operation,
                    userId = userId
                )
                taskPendingSyncDao.upsertTaskPendingSyncEntity(pendingTask)
            }

            AgendaKind.EVENT -> {

            }

            AgendaKind.REMINDER -> {

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
            .setInputData(data)
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
        WorkManager.Companion.getInstance(context)
            .cancelAllWork()
            .await()
    }
}
