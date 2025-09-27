package com.example.tasky.agenda.domain.data.sync

import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.core.data.database.SyncOperation
import kotlin.time.Duration

interface SyncAgendaItemScheduler {

    suspend fun scheduleSync(type: SyncType)
    suspend fun cancelAllSyncs()

    sealed interface SyncType {
        data class FetchAgendaItems(val interval: Duration) : SyncType
        data class DeleteAgendaItem(val item: AgendaItem) : SyncType
        class UpsertAgendaItem(val item: AgendaItem, val operation: SyncOperation) : SyncType
    }
}
