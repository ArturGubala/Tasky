package com.example.tasky.agenda.data.repository

import com.example.tasky.agenda.domain.data.TaskRepository
import com.example.tasky.agenda.domain.data.network.TaskRemoteDataSource
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.core.domain.data.TaskLocalDataStore
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.domain.util.asEmptyDataResult
import com.example.tasky.core.domain.util.onError
import com.example.tasky.core.domain.util.onSuccess

class OfflineFirstTaskRepository(
    private val taskRemoteDataSource: TaskRemoteDataSource,
    private val taskLocalDataStore: TaskLocalDataStore,
) : TaskRepository {

    override suspend fun createTask(task: Task): EmptyResult<DataError> {
        val localResult = taskLocalDataStore.upsertTask(task)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }

        return taskRemoteDataSource.createTask(task)
            .onSuccess {}.asEmptyDataResult()
            .onError { error ->
                // TODO write task to sync queue
            }.asEmptyDataResult()
    }
}
