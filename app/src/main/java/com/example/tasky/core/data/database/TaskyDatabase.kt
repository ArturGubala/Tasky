package com.example.tasky.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tasky.core.data.database.reminder.dao.ReminderDao
import com.example.tasky.core.data.database.reminder.dao.ReminderPendingSyncDao
import com.example.tasky.core.data.database.reminder.entity.ReminderDeletedSyncEntity
import com.example.tasky.core.data.database.reminder.entity.ReminderEntity
import com.example.tasky.core.data.database.reminder.entity.ReminderPendingSyncEntity
import com.example.tasky.core.data.database.task.dao.TaskDao
import com.example.tasky.core.data.database.task.dao.TaskPendingSyncDao
import com.example.tasky.core.data.database.task.entity.TaskDeletedSyncEntity
import com.example.tasky.core.data.database.task.entity.TaskEntity
import com.example.tasky.core.data.database.task.entity.TaskPendingSyncEntity

@Database(
    entities = [
        TaskEntity::class,
        TaskPendingSyncEntity::class,
        TaskDeletedSyncEntity::class,
        ReminderEntity::class,
        ReminderPendingSyncEntity::class,
        ReminderDeletedSyncEntity::class,
    ],
    version = 1,
)

abstract class TaskyDatabase : RoomDatabase() {

    abstract val taskDao: TaskDao
    abstract val taskPendingSyncDao: TaskPendingSyncDao
    abstract val reminderDao: ReminderDao
    abstract val reminderPendingSyncDao: ReminderPendingSyncDao

}
