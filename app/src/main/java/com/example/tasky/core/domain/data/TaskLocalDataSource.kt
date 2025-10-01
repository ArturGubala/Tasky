package com.example.tasky.core.domain.data

import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface TaskLocalDataSource {
    fun getTask(id: String): Flow<Task>
    fun getTasksForDay(startOfDay: Long, endOfDay: Long): Flow<List<Task>>
    suspend fun upsertTask(task: Task): EmptyResult<DataError.Local>

    suspend fun insertTasks(tasks: List<Task>): EmptyResult<DataError.Local>
    suspend fun deleteTask(id: String)
    suspend fun deleteTasks()
}
