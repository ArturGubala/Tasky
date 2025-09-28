package com.example.tasky.core.data.database.event

import android.database.sqlite.SQLiteFullException
import com.example.Eventy.core.data.database.event.dao.EventDao
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.core.data.database.event.mappers.toAttendeeEntities
import com.example.tasky.core.data.database.event.mappers.toEvent
import com.example.tasky.core.data.database.event.mappers.toEventEntity
import com.example.tasky.core.data.database.event.mappers.toPhotoEntities
import com.example.tasky.core.domain.data.EventLocalDataSource
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalEventDataSource(
    private val eventDao: EventDao,
) : EventLocalDataSource {
    override fun getEvent(id: String): Flow<Event> {
        return eventDao.getEvent(id = id)
            .map { it.toEvent() }
    }

    override fun getEventForDay(
        startOfDay: Long,
        endOfDay: Long,
    ): Flow<List<Event>> {
        return eventDao.getEventsForDay(startOfDay = startOfDay, endOfDay = endOfDay)
            .map { eventEntities ->
                eventEntities.map { it.toEvent() }
            }
    }

    override suspend fun upsertEvent(event: Event): EmptyResult<DataError.Local> {
        return try {
            val entity = event.toEventEntity()
            eventDao.upsertEvent(entity)
            Result.Success(Unit)
        } catch (_: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun insertEvents(events: List<Event>): EmptyResult<DataError.Local> {
        return try {
            val eventEntities = events.map { it.toEventEntity() }
            val allAttendeesEntities = events.flatMap { it.toAttendeeEntities() }
            val allPhotosEntities = events.flatMap { it.toPhotoEntities() }

            eventDao.insertEventsWithRelations(
                events = eventEntities,
                attendees = allAttendeesEntities,
                photos = allPhotosEntities
            )

            Result.Success(Unit)
        } catch (_: SQLiteFullException) {
            Result.Success(Unit)
        }
    }

    override suspend fun deleteEvent(id: String) {
        eventDao.deleteEvent(id)
    }
}
