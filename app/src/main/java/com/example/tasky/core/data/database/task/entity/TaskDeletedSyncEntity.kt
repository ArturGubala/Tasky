package com.example.tasky.core.data.database.task.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_delete_pending_sync")
data class TaskDeletedSyncEntity(
    @PrimaryKey(autoGenerate = false)
    val taskId: String,
    val userId: String,
)
