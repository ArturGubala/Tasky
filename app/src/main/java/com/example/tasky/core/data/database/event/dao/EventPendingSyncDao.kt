package com.example.tasky.core.data.database.event.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.data.database.event.entity.EventDeletedSyncEntity
import com.example.tasky.core.data.database.event.entity.EventPendingSyncEntity

@Dao
interface EventPendingSyncDao {

    // UPSERT EVENT
    @Query("SELECT * FROM event_upsert_pending_sync WHERE userId = :userId")
    suspend fun getAllEventPendingSyncEntities(userId: String): List<EventPendingSyncEntity>

    @Query("SELECT * FROM event_upsert_pending_sync WHERE eventId = :eventId")
    suspend fun getEventPendingSyncEntity(eventId: String): EventPendingSyncEntity?

    @Query("SELECT * FROM event_upsert_pending_sync WHERE eventId = :eventId AND operation = :operation")
    suspend fun getEventPendingSyncEntityByIdAndOperation(
        eventId: String,
        operation: SyncOperation,
    ): EventPendingSyncEntity?

    @Upsert
    suspend fun upsertEventPendingSyncEntity(entity: EventPendingSyncEntity)

    @Query("DELETE FROM event_upsert_pending_sync WHERE eventId = :eventId AND operation IN (:operations)")
    suspend fun deleteEventPendingSyncEntity(eventId: String, operations: List<SyncOperation>)

    // DELETED EVENT
    @Query("SELECT * FROM event_delete_pending_sync WHERE userId = :userId")
    suspend fun getAllDeletedEventSyncEntities(userId: String): List<EventDeletedSyncEntity>

    @Upsert
    suspend fun upsertDeletedEventSyncEntity(entity: EventDeletedSyncEntity)

    @Query("DELETE FROM event_delete_pending_sync WHERE eventId = :eventId")
    suspend fun deleteDeletedEventSyncEntity(eventId: String)
}
