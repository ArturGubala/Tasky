package com.example.tasky.agenda.domain.data

import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult

interface TaskRepository {
    suspend fun createTask(task: Task): EmptyResult<DataError>
}
