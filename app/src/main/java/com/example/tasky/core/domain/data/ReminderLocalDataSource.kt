package com.example.tasky.core.domain.data

import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface ReminderLocalDataSource {
    fun getReminder(id: String): Flow<Reminder>
    fun getRemindersIds(): Flow<List<String>>
    fun getReminderForDay(startOfDay: Long, endOfDay: Long): Flow<List<Reminder>>
    suspend fun upsertReminder(reminder: Reminder): EmptyResult<DataError.Local>
    suspend fun insertReminders(reminders: List<Reminder>): EmptyResult<DataError.Local>
    suspend fun deleteReminder(id: String)
    suspend fun deleteReminders()
}
