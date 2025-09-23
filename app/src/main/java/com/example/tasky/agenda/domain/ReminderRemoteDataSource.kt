package com.example.tasky.agenda.domain

import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.Result

interface ReminderRemoteDataSource {
    suspend fun getReminder(id: String): Result<Reminder, DataError.Network>
    suspend fun createReminder(reminder: Reminder): Result<Reminder, DataError.Network>
    suspend fun updateReminder(reminder: Reminder): Result<Reminder, DataError.Network>
    suspend fun deleteReminder(id: String): EmptyResult<DataError.Network>
}
