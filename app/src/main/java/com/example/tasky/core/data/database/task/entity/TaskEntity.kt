package com.example.tasky.core.data.database.task.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long,
    val updatedAt: Long?,
    val isDone: Boolean,
    val isSynced: Boolean = false,
)
