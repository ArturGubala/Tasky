package com.example.tasky.agenda.data.network

import com.example.tasky.agenda.data.network.dto.task.CreateTaskRequest
import com.example.tasky.agenda.data.network.dto.task.TaskDto
import com.example.tasky.agenda.data.network.dto.task.UpdateTaskRequest
import com.example.tasky.agenda.data.network.mappers.toCreateTaskRequest
import com.example.tasky.agenda.data.network.mappers.toTask
import com.example.tasky.agenda.data.network.mappers.toUpdateTaskRequest
import com.example.tasky.agenda.domain.RemoteAgendaDataSource
import com.example.tasky.agenda.domain.model.Attendee
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
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

class KtorAgendaDataSource(
    private val httpClient: HttpClient,
) : RemoteAgendaDataSource {

    override suspend fun getTask(id: String): Result<Task, DataError.Network> {
        return httpClient.get<TaskDto>(
            route = "/task/$id",
        ).map { it.toTask() }
    }

    override suspend fun createTask(task: Task): Result<Task, DataError.Network> {
        return httpClient.post<CreateTaskRequest, TaskDto>(
            route = "/task",
            body = task.toCreateTaskRequest()
        ).map { it.toTask() }
    }

    override suspend fun updateTask(task: Task): Result<Task, DataError.Network> {
        return httpClient.put<UpdateTaskRequest, TaskDto>(
            route = "/task",
            body = task.toUpdateTaskRequest()
        ).map { it.toTask() }
    }

    override suspend fun deleteTask(id: String): EmptyResult<DataError.Network> {
        return httpClient.delete(route = "/task/$id")
    }

    override suspend fun getReminder(id: String): Result<Reminder, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun createReminder(reminder: Reminder): Result<Reminder, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun updateReminder(reminder: Reminder): Result<Reminder, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReminder(id: String): EmptyResult<DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun getEvent(id: String): Result<Event, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun createEvent(event: Event): Result<Event, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun updateEvent(event: Event): Result<Event, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvent(id: String): EmptyResult<DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun confirmUpload(ids: List<String>): Result<Event, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun getAttendee(email: String): Result<Attendee, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAttendee(id: String): EmptyResult<DataError.Network> {
        TODO("Not yet implemented")
    }
}
