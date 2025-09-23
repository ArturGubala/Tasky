@file:OptIn(ExperimentalTime::class)

package com.example.tasky.agenda.data.network.mappers

import com.example.tasky.agenda.data.network.dto.task.CreateTaskRequest
import com.example.tasky.agenda.data.network.dto.task.TaskDto
import com.example.tasky.agenda.data.network.dto.task.UpdateTaskRequest
import com.example.tasky.agenda.domain.model.Task
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun TaskDto.toTask(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        time = Instant.parse(time),
        remindAt = Instant.parse(remindAt),
        updatedAt = updatedAt?.let { Instant.parse(it) },
        isDone = isDone
    )
}

fun Task.toCreateTaskRequest(): CreateTaskRequest {
    return CreateTaskRequest(
        id = id,
        title = title,
        description = description,
        time = time.toString(),
        remindAt = remindAt.toString(),
        updatedAt = updatedAt?.toString(),
        isDone = isDone
    )
}

fun Task.toUpdateTaskRequest(): UpdateTaskRequest {
    return UpdateTaskRequest(
        id = id,
        title = title,
        description = description,
        time = time.toString(),
        remindAt = remindAt.toString(),
        isDone = isDone
    )
}
