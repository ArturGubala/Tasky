package com.example.tasky.agenda.domain.data.network

import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.Result

interface TaskRemoteDataSource {
    suspend fun getTask(id: String): Result<Task, DataError.Network>
    suspend fun createTask(task: Task): Result<Task, DataError.Network>
    suspend fun updateTask(task: Task): Result<Task, DataError.Network>
    suspend fun deleteTask(id: String): EmptyResult<DataError.Network>
}
