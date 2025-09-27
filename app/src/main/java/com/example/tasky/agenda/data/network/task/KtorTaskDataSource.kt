package com.example.tasky.agenda.data.network.task

import com.example.tasky.agenda.data.network.task.dto.TaskDto
import com.example.tasky.agenda.data.network.task.dto.UpsertTaskRequest
import com.example.tasky.agenda.data.network.task.mappers.toTask
import com.example.tasky.agenda.data.network.task.mappers.toUpsertTaskRequest
import com.example.tasky.agenda.domain.data.network.TaskRemoteDataSource
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.core.data.networking.delete
import com.example.tasky.core.data.networking.get
import com.example.tasky.core.data.networking.post
import com.example.tasky.core.data.networking.put
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.domain.util.map
import io.ktor.client.HttpClient

class KtorTaskDataSource(
    private val httpClient: HttpClient,
) : TaskRemoteDataSource {

    override suspend fun getTask(id: String): Result<Task, DataError.Network> {
        return httpClient.get<TaskDto>(
            route = "/task/$id",
        ).map { it.toTask() }
    }

    override suspend fun createTask(task: Task): Result<Task, DataError.Network> {
        return httpClient.post<UpsertTaskRequest, TaskDto>(
            route = "/task",
            body = task.toUpsertTaskRequest()
        ).map { it.toTask() }
    }

    override suspend fun updateTask(task: Task): Result<Task, DataError.Network> {
        return httpClient.put<UpsertTaskRequest, TaskDto>(
            route = "/task",
            body = task.toUpsertTaskRequest()
        ).map { it.toTask() }
    }

    override suspend fun deleteTask(id: String): EmptyResult<DataError.Network> {
        return httpClient.delete(route = "/task/$id")
    }
}
