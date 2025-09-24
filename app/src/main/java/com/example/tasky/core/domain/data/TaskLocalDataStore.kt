package com.example.tasky.core.domain.data

import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult

interface TaskLocalDataStore {
    suspend fun getTask(id: String): Task
    suspend fun getTasksForDay(startOfDay: Long, endOfDay: Long): List<Task>
    suspend fun upsertTask(task: Task): EmptyResult<DataError.Local>
    suspend fun deleteTask(id: String)
}
