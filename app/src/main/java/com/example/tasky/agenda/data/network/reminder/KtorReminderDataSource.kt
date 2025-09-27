package com.example.tasky.agenda.data.network.reminder

import com.example.tasky.agenda.data.network.reminder.dto.ReminderDto
import com.example.tasky.agenda.data.network.reminder.dto.UpsertReminderRequest
import com.example.tasky.agenda.data.network.reminder.mappers.toReminder
import com.example.tasky.agenda.data.network.reminder.mappers.toUpsertReminderRequest
import com.example.tasky.agenda.domain.data.network.ReminderRemoteDataSource
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.core.data.networking.delete
import com.example.tasky.core.data.networking.get
import com.example.tasky.core.data.networking.post
import com.example.tasky.core.data.networking.put
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.domain.util.map
import io.ktor.client.HttpClient

class KtorReminderDataSource(
    private val httpClient: HttpClient,
) : ReminderRemoteDataSource {
    override suspend fun getReminder(id: String): Result<Reminder, DataError.Network> {
        return httpClient.get<ReminderDto>(
            route = "/reminder/$id",
        ).map { it.toReminder() }
    }

    override suspend fun createReminder(reminder: Reminder): Result<Reminder, DataError.Network> {
        return httpClient.post<UpsertReminderRequest, ReminderDto>(
            route = "/reminder",
            body = reminder.toUpsertReminderRequest()
        ).map { it.toReminder() }
    }

    override suspend fun updateReminder(reminder: Reminder): Result<Reminder, DataError.Network> {
        return httpClient.put<UpsertReminderRequest, ReminderDto>(
            route = "/reminder",
            body = reminder.toUpsertReminderRequest()
        ).map { it.toReminder() }
    }

    override suspend fun deleteReminder(id: String): EmptyResult<DataError.Network> {
        return httpClient.delete(route = "/reminder/$id")
    }
}
