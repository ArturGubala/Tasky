package com.example.tasky.agenda.data.sync

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import androidx.work.workDataOf
import com.example.tasky.agenda.domain.data.sync.SyncAgendaItemScheduler
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.kind
import com.example.tasky.agenda.domain.util.AgendaKind
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.data.database.event.dao.EventPendingSyncDao
import com.example.tasky.core.data.database.event.entity.EventDeletedSyncEntity
import com.example.tasky.core.data.database.event.entity.EventPendingSyncEntity
import com.example.tasky.core.data.database.event.mappers.toEventEntity
import com.example.tasky.core.data.database.reminder.dao.ReminderPendingSyncDao
import com.example.tasky.core.data.database.reminder.entity.ReminderDeletedSyncEntity
import com.example.tasky.core.data.database.reminder.entity.ReminderPendingSyncEntity
import com.example.tasky.core.data.database.reminder.mappers.toReminderEntity
import com.example.tasky.core.data.database.task.dao.TaskPendingSyncDao
import com.example.tasky.core.data.database.task.entity.TaskDeletedSyncEntity
import com.example.tasky.core.data.database.task.entity.TaskPendingSyncEntity
import com.example.tasky.core.data.database.task.mappers.toTaskEntity
import com.example.tasky.core.domain.data.EventLocalDataSource
import com.example.tasky.core.domain.data.ReminderLocalDataSource
import com.example.tasky.core.domain.data.TaskLocalDataSource
import com.example.tasky.core.domain.datastore.SessionStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SyncAgendaItemWorkerScheduler(
    private val context: Context,
    private val taskLocalDataSource: TaskLocalDataSource,
    private val taskPendingSyncDao: TaskPendingSyncDao,
    private val reminderLocalDataSource: ReminderLocalDataSource,
    private val reminderPendingSyncDao: ReminderPendingSyncDao,
    private val eventLocalDataSource: EventLocalDataSource,
    private val eventPendingSyncDao: EventPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope,
) : SyncAgendaItemScheduler {

    private val workManager = WorkManager.Companion.getInstance(context)

    override suspend fun scheduleSync(type: SyncAgendaItemScheduler.SyncType) {
        when (type) {
            is SyncAgendaItemScheduler.SyncType.FetchAgendaItems ->
                scheduleFetch(type.interval)
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
                val pendingEvent = EventDeletedSyncEntity(
                    eventId = item.id,
                    userId = userId
                )
                eventPendingSyncDao.upsertDeletedEventSyncEntity(pendingEvent)
            }

            AgendaKind.REMINDER -> {
                val pendingReminder = ReminderDeletedSyncEntity(
                    reminderId = item.id,
                    userId = userId
                )
                reminderPendingSyncDao.upsertDeletedReminderSyncEntity(pendingReminder)
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
                val task = taskLocalDataSource.getTask(id = item.id).firstOrNull() ?: return
                val pendingTask = TaskPendingSyncEntity(
                    task = task.toTaskEntity(),
                    operation = operation,
                    userId = userId
                )
                taskPendingSyncDao.upsertTaskPendingSyncEntity(pendingTask)
            }

            AgendaKind.EVENT -> {
                val event = eventLocalDataSource.getEvent(id = item.id).firstOrNull() ?: return
                val pendingEvent = EventPendingSyncEntity(
                    event = event.toEventEntity(),
                    operation = operation,
                    userId = userId
                )
                eventPendingSyncDao.upsertEventPendingSyncEntity(pendingEvent)
            }

            AgendaKind.REMINDER -> {
                val reminder =
                    reminderLocalDataSource.getReminder(id = item.id).firstOrNull() ?: return
                val pendingReminder = ReminderPendingSyncEntity(
                    reminder = reminder.toReminderEntity(),
                    operation = operation,
                    userId = userId
                )
                reminderPendingSyncDao.upsertReminderPendingSyncEntity(pendingReminder)
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

    private suspend fun scheduleFetch(interval: Duration) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workManager
                .getWorkInfosByTag("sync_work")
                .get()
                .isNotEmpty()
        }
        if (isSyncScheduled) {
            return
        }

        val workRequest = PeriodicWorkRequestBuilder<FetchAgendaItemsWorker>(
            repeatInterval = interval.toJavaDuration()
        )
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
            .setInitialDelay(
                duration = 30,
                timeUnit = TimeUnit.MINUTES
            )
            .addTag("sync_work")
            .build()

        workManager.enqueue(workRequest).await()
    }

    override suspend fun cancelAllSyncs() {
        WorkManager.Companion.getInstance(context)
            .cancelAllWork()
            .await()
    }
}
