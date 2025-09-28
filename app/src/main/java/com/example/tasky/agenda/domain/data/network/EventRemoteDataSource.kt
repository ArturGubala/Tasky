package com.example.tasky.agenda.domain.data.network

import com.example.tasky.agenda.domain.model.Attendee
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.Result

interface EventRemoteDataSource {
    suspend fun getEvent(id: String): Result<Event, DataError.Network>
    suspend fun createEvent(event: Event): Result<Event, DataError.Network>
    suspend fun updateEvent(event: Event): Result<Event, DataError.Network>
    suspend fun deleteEvent(id: String): EmptyResult<DataError.Network>
    suspend fun confirmUpload(ids: List<String>): Result<Event, DataError.Network>

    suspend fun getAttendee(email: String): Result<Attendee, DataError.Network>
    suspend fun deleteAttendee(eventId: String): EmptyResult<DataError.Network>
}
