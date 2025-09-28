package com.example.Eventy.core.data.database.event.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Transaction
    suspend fun insertEventsWithRelations(
        events: List<EventEntity>,
        attendees: List<AttendeeEntity>,
        photos: List<PhotoEntity>,
    ) {
        insertEvents(events)
        insertAttendees(attendees)
        insertPhotos(photos)
    }

    @Query("SELECT * FROM Event WHERE id = :id")
    fun getEvent(id: String): Flow<EventWithRelations>

    @Query(
        """
        SELECT * FROM Event
        WHERE timeFrom >= :startOfDay AND timeFrom < :endOfDay
    """
    )
    fun getEventsForDay(startOfDay: Long, endOfDay: Long): Flow<List<EventEntity>>

    @Query("DELETE FROM Event WHERE id = :id")
    suspend fun deleteEvent(id: String)


    // ATTENDEE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendees(attendees: List<AttendeeEntity>)

    @Query("DELETE FROM attendee WHERE userId = :userId and eventId = :eventId")
    suspend fun deleteAttendee(userId: String, eventId: String)


    // PHOTO
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<PhotoEntity>)

    @Query("DELETE FROM photo WHERE `key` = :key and eventId = :eventId")
    suspend fun deletePhoto(key: String, eventId: String)
}
