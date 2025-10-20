package com.example.Eventy.core.data.database.event.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.tasky.core.data.database.event.entity.AttendeeEntity
import com.example.tasky.core.data.database.event.entity.EventEntity
import com.example.tasky.core.data.database.event.entity.PhotoEntity
import com.example.tasky.core.data.database.event.util.EventWithRelations
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    // EVENT
    @Upsert
    suspend fun upsertEvent(event: EventEntity)

    @Upsert
    suspend fun upsertEvents(events: List<EventEntity>)

    @Transaction
    suspend fun upsertEventWithRelations(
        event: EventEntity,
        attendees: List<AttendeeEntity>,
        photos: List<PhotoEntity>,
    ) {
        upsertEvent(event)
        upsertAttendees(attendees)
        upsertPhotos(photos)
    }

    @Transaction
    suspend fun upsertEventsWithRelations(
        events: List<EventEntity>,
        attendees: List<AttendeeEntity>,
        photos: List<PhotoEntity>,
    ) {
        upsertEvents(events)
        upsertAttendees(attendees)
        upsertPhotos(photos)
    }

    @Query("SELECT * FROM Event WHERE id = :id")
    fun getEvent(id: String): Flow<EventWithRelations>

    @Query("SELECT id FROM Event")
    fun getEventsIds(): Flow<List<String>>

    @Query(
        """
        SELECT * FROM Event
        WHERE timeFrom >= :startOfDay AND timeFrom < :endOfDay
    """
    )
    fun getEventsForDay(startOfDay: Long, endOfDay: Long): Flow<List<EventEntity>>

    @Query("DELETE FROM Event WHERE id = :id")
    suspend fun deleteEvent(id: String)

    @Query("DELETE FROM event")
    suspend fun deleteEvents()


    // ATTENDEE
    @Upsert
    suspend fun upsertAttendees(attendees: List<AttendeeEntity>)

    @Query("DELETE FROM attendee WHERE userId = :userId and eventId = :eventId")
    suspend fun deleteAttendee(userId: String, eventId: String)

    // PHOTO
    @Upsert
    suspend fun upsertPhotos(photos: List<PhotoEntity>)

    @Query("DELETE FROM photo WHERE `key` = :key and eventId = :eventId")
    suspend fun deletePhoto(key: String, eventId: String)

    @Query("DELETE FROM photo WHERE eventId = :eventId")
    suspend fun deletePhotosForEvent(eventId: String)
}
