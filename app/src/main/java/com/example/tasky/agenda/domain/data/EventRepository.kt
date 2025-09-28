package com.example.tasky.agenda.domain.data

import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult

interface EventRepository {
    suspend fun upsertEvent(event: Event, syncOperation: SyncOperation): EmptyResult<DataError>
    suspend fun deleteEvent(id: String): EmptyResult<DataError>
    suspend fun syncPendingEvent()
}
