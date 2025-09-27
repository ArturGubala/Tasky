package com.example.tasky.agenda.domain.data

import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult

interface TaskRepository {
    suspend fun upsertTask(task: Task, syncOperation: SyncOperation): EmptyResult<DataError>
    suspend fun deleteTask(id: String): EmptyResult<DataError>
    suspend fun syncPendingTask()
}
