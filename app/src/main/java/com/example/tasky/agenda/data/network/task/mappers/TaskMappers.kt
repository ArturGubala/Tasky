@file:OptIn(ExperimentalTime::class)

package com.example.tasky.agenda.data.network.task.mappers

import com.example.tasky.agenda.data.network.task.dto.TaskDto
import com.example.tasky.agenda.data.network.task.dto.UpsertTaskRequest
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

fun Task.toUpsertTaskRequest(): UpsertTaskRequest {
    return UpsertTaskRequest(
        id = id,
        title = title,
        description = description,
        time = time.toString(),
        remindAt = remindAt.toString(),
        updatedAt = updatedAt?.toString(),
        isDone = isDone
    )
}
