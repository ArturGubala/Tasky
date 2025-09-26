package com.example.tasky.core.data.database.reminder.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.data.database.reminder.entity.ReminderDeletedSyncEntity
import com.example.tasky.core.data.database.reminder.entity.ReminderPendingSyncEntity

@Dao
interface ReminderPendingSyncDao {

    // UPSERT REMINDER
    @Query("SELECT * FROM reminder_upsert_pending_sync WHERE userId = :userId")
    suspend fun getAllReminderPendingSyncEntities(userId: String): List<ReminderPendingSyncEntity>

    @Query("SELECT * FROM reminder_upsert_pending_sync WHERE reminderId = :reminderId")
    suspend fun getTaskPendingSyncEntity(reminderId: String): ReminderPendingSyncEntity?

    @Query(
        "SELECT * FROM reminder_upsert_pending_sync " +
                "WHERE reminderId = :reminderId AND operation = :operation"
    )
    suspend fun getReminderPendingSyncEntityByIdAndOperation(
        reminderId: String,
        operation: SyncOperation,
    ): ReminderPendingSyncEntity?

    @Upsert
    suspend fun upsertReminderPendingSyncEntity(entity: ReminderPendingSyncEntity)

    @Query("DELETE FROM reminder_upsert_pending_sync WHERE reminderId = :reminderId AND operation IN (:operations)")
    suspend fun deleteReminderPendingSyncEntity(reminderId: String, operations: List<SyncOperation>)

    // DELETED REMINDER
    @Query("SELECT * FROM reminder_delete_pending_sync WHERE userId = :userId")
    suspend fun getAllDeletedReminderSyncEntities(userId: String): List<ReminderDeletedSyncEntity>

    @Upsert
    suspend fun upsertDeletedReminderSyncEntity(entity: ReminderDeletedSyncEntity)

    @Query("DELETE FROM reminder_delete_pending_sync WHERE reminderId = :reminderId")
    suspend fun deleteDeletedReminderSyncEntity(reminderId: String)
}
