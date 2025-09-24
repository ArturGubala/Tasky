package com.example.tasky.core.data.database.task

import android.database.sqlite.SQLiteFullException
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.core.data.database.task.dao.TaskDao
import com.example.tasky.core.data.database.task.mappers.toTask
import com.example.tasky.core.data.database.task.mappers.toTaskEntity
import com.example.tasky.core.domain.data.TaskLocalDataStore
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.Result

class RoomLocalTaskDataSource(
    private val taskDao: TaskDao,
) : TaskLocalDataStore {

    override suspend fun getTask(id: String): Task {
        return taskDao.getTask(id = id).toTask()
    }

    override suspend fun getTasksForDay(
        startOfDay: Long,
        endOfDay: Long,
    ): List<Task> {
        return taskDao.getTasksForDay(startOfDay = startOfDay, endOfDay = endOfDay)
            .map { it.toTask() }
    }

    override suspend fun upsertTask(task: Task): EmptyResult<DataError.Local> {
        return try {
            val entity = task.toTaskEntity()
            taskDao.upsertTask(entity)
            Result.Success(Unit)
        } catch (_: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteTask(id: String) {
        taskDao.deleteTask(id)
    }
}
