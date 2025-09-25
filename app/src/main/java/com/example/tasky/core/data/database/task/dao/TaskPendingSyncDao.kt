package com.example.tasky.core.data.database.task.dao

import androidx.room.Query
import androidx.room.Upsert
import com.example.tasky.core.data.database.task.entity.TaskDeletedSyncEntity
import com.example.tasky.core.data.database.task.entity.TaskPendingSyncEntity

interface TaskPendingSyncDao {

    // UPSERT TASK
    @Query("SELECT * FROM task_upsert_pending_sync WHERE userId = :userId")
    suspend fun getAllTaskPendingSyncEntities(userId: String): List<TaskPendingSyncEntity>

    @Query("SELECT * FROM task_upsert_pending_sync WHERE taskId = :taskId")
    suspend fun getTaskPendingSyncEntity(taskId: String): TaskPendingSyncEntity?

    @Upsert
    suspend fun upsertTaskPendingSyncEntity(entity: TaskPendingSyncEntity)

    @Query("DELETE FROM task_upsert_pending_sync WHERE taskId = :taskId")
    suspend fun deleteTaskPendingSyncEntity(taskId: String)


    // DELETED RUNS
    @Query("SELECT * FROM task_delete_pending_sync WHERE userId = :userId")
    suspend fun getAllDeletedTaskSyncEntities(userId: String): List<TaskDeletedSyncEntity>

    @Upsert
    suspend fun upsertDeletedTaskSyncEntity(entity: TaskDeletedSyncEntity)

    @Query("DELETE FROM task_delete_pending_sync WHERE taskId = :taskId")
    suspend fun deleteDeletedTaskSyncEntity(taskId: String)
}
