package com.example.tasky.core.data.database.task.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tasky.core.data.database.SyncOperation

@Entity(tableName = "task_upsert_pending_sync")
data class TaskPendingSyncEntity(
    @Embedded val task: TaskEntity,
    @PrimaryKey(autoGenerate = false)
    val taskId: String = task.id,
    val operation: SyncOperation,
    val userId: String,
)
