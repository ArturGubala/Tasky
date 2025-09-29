package com.example.tasky.core.data.database.task

import android.database.sqlite.SQLiteFullException
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.core.data.database.task.dao.TaskDao
import com.example.tasky.core.data.database.task.mappers.toTask
import com.example.tasky.core.data.database.task.mappers.toTaskEntity
import com.example.tasky.core.domain.data.TaskLocalDataSource
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalTaskDataSource(
    private val taskDao: TaskDao,
) : TaskLocalDataSource {

    override fun getTask(id: String): Flow<Task> {
        return taskDao.getTask(id = id)
            .map { it.toTask() }
    }

    override fun getTasksForDay(
        startOfDay: Long,
        endOfDay: Long,
    ): Flow<List<Task>> {
        return taskDao.getTasksForDay(startOfDay = startOfDay, endOfDay = endOfDay)
            .map { taskEntities ->
                taskEntities.map { it.toTask() }
            }
    }

    override suspend fun upsertTask(task: Task): EmptyResult<DataError.Local> {
        return try {
            val entity = task.toTaskEntity()
            taskDao.upsertTask(entity)
            Result.Success(Unit)
        } catch (_: SQLiteFullException) {
            Result.Error(DataError.Local.DiskFull)
        }
    }

    override suspend fun insertTasks(tasks: List<Task>): EmptyResult<DataError.Local> {
        return try {
            val entities = tasks.map { it.toTaskEntity() }
            taskDao.insertTasks(entities)
            Result.Success(Unit)
        } catch (_: SQLiteFullException) {
            Result.Success(Unit)
        }
    }

    override suspend fun deleteTask(id: String) {
        taskDao.deleteTask(id)
    }
}
