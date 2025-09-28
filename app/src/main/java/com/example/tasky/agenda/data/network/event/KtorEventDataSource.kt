package com.example.tasky.agenda.data.network.event

import com.example.tasky.agenda.data.network.event.dto.AttendeeDto
import com.example.tasky.agenda.data.network.event.dto.CreateEventRequest
import com.example.tasky.agenda.data.network.event.dto.EventDto
import com.example.tasky.agenda.data.network.event.dto.UpdateEventRequest
import com.example.tasky.agenda.data.network.event.mappers.toAttendee
import com.example.tasky.agenda.data.network.event.mappers.toCreateEventRequest
import com.example.tasky.agenda.data.network.event.mappers.toEvent
import com.example.tasky.agenda.data.network.event.mappers.toUpdateEventRequest
import com.example.tasky.agenda.domain.data.network.EventRemoteDataSource
import com.example.tasky.agenda.domain.model.Attendee
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.core.data.networking.delete
import com.example.tasky.core.data.networking.get
import com.example.tasky.core.data.networking.post
import com.example.tasky.core.data.networking.put
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.domain.util.map
import io.ktor.client.HttpClient

class KtorEventDataSource(
    private val httpClient: HttpClient,
) : EventRemoteDataSource {

    override suspend fun getEvent(id: String): Result<Event, DataError.Network> {
        return httpClient.get<EventDto>(
            route = "/event/$id",
        ).map { it.toEvent() }
    }

    override suspend fun createEvent(event: Event): Result<Event, DataError.Network> {
        return httpClient.post<CreateEventRequest, EventDto>(
            route = "/event",
            body = event.toCreateEventRequest()
        ).map { it.toEvent() }
    }

    override suspend fun updateEvent(event: Event): Result<Event, DataError.Network> {
        return httpClient.put<UpdateEventRequest, EventDto>(
            route = "/event",
            body = event.toUpdateEventRequest()
        ).map { it.toEvent() }
    }

    override suspend fun deleteEvent(id: String): EmptyResult<DataError.Network> {
        return httpClient.delete(route = "/event/$id")
    }

    override suspend fun confirmUpload(ids: List<String>): Result<Event, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun getAttendee(email: String): Result<Attendee, DataError.Network> {
        return httpClient.get<AttendeeDto>(
            route = "/attendee",
            queryParameters = mapOf("email" to email)
        ).map { it.toAttendee() }
    }

    override suspend fun deleteAttendee(eventId: String): EmptyResult<DataError.Network> {
        return httpClient.delete(
            route = "/event",
            queryParameters = mapOf(
                "eventId" to eventId
            )
        )
    }
}
