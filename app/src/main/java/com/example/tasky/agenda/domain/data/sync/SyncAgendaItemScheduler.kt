package com.example.tasky.agenda.domain.data.sync

import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.agenda.domain.util.AgendaItemType
import com.example.tasky.core.data.database.SyncOperation
import kotlin.time.Duration

interface SyncAgendaItemScheduler {

    suspend fun scheduleSync(type: SyncType)
    suspend fun cancelAllSyncs()

    sealed interface SyncType {
        data class FetchAgendaItem(val interval: Duration) : SyncType
        data class DeleteAgendaItem(val taskId: String) : SyncType
        class UpsertAgendaItem(
            val task: Task, val operation: SyncOperation,
            val itemType: AgendaItemType,
        ) : SyncType
    }
}
