package com.example.Eventy.core.data.database.event.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.tasky.core.data.database.event.entity.EventEntity
import kotlinx.coroutines.flow.Flow

interface EventDao {

    @Upsert
    suspend fun upsertEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Query("SELECT * FROM Event WHERE id = :id")
    fun getEvent(id: String): Flow<EventEntity>

    @Query(
        """
        SELECT * FROM Event
        WHERE timeFrom >= :startOfDay AND timeFrom < :endOfDay
    """
    )
    fun getEventsForDay(startOfDay: Long, endOfDay: Long): Flow<List<EventEntity>>

    @Query("DELETE FROM Event WHERE id = :id")
    suspend fun deleteEvent(id: String)
}
