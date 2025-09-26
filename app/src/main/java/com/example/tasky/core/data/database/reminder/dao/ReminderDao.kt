package com.example.tasky.core.data.database.reminder.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.tasky.core.data.database.reminder.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Upsert
    suspend fun upsertReminder(reminder: ReminderEntity)

    @Query("SELECT * FROM reminder WHERE id = :id")
    fun getReminder(id: String): Flow<ReminderEntity>

    @Query(
        """
        SELECT * FROM reminder
        WHERE time >= :startOfDay AND time < :endOfDay
    """
    )
    fun getRemindersForDay(startOfDay: Long, endOfDay: Long): Flow<List<ReminderEntity>>

    @Query("DELETE FROM reminder WHERE id = :id")
    suspend fun deleteReminder(id: String)
}
