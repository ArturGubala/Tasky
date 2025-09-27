package com.example.tasky.agenda.domain.data

import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult

interface ReminderRepository {
    suspend fun upsertReminder(
        reminder: Reminder,
        syncOperation: SyncOperation,
    ): EmptyResult<DataError>

    suspend fun deleteReminder(id: String): EmptyResult<DataError>
    suspend fun syncPendingReminders()
}
