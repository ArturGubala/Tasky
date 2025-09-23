package com.example.tasky.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tasky.core.data.database.task.dao.TaskDao
import com.example.tasky.core.data.database.task.entity.TaskEntity

@Database(
    entities = [
        TaskEntity::class
    ],
    version = 1,
)

abstract class TaskyDatabase : RoomDatabase() {

    abstract val taskDao: TaskDao
}
