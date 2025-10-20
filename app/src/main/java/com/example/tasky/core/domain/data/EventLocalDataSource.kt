package com.example.tasky.core.domain.data

import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface EventLocalDataSource {
    fun getEvent(id: String, userId: String = ""): Flow<Event>
    fun getEventsIds(): Flow<List<String>>
    fun getEventForDay(startOfDay: Long, endOfDay: Long): Flow<List<Event>>
    suspend fun upsertEvent(event: Event): EmptyResult<DataError.Local>
    suspend fun insertEvents(events: List<Event>): EmptyResult<DataError.Local>
    suspend fun deleteEvent(id: String)
    suspend fun deleteEvents()

    suspend fun deleteAttendee(userId: String, eventId: String)
}
