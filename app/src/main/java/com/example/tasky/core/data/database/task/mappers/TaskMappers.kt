@file:OptIn(ExperimentalTime::class)

package com.example.tasky.core.data.database.task.mappers

import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.core.data.database.task.entity.TaskEntity
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun TaskEntity.toTask(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        time = Instant.fromEpochMilliseconds(time),
        remindAt = Instant.fromEpochMilliseconds(remindAt),
        updatedAt = updatedAt?.let { Instant.fromEpochMilliseconds(updatedAt) },
        isDone = isDone
    )
}

fun Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        time = time.toEpochMilliseconds(),
        remindAt = remindAt.toEpochMilliseconds(),
        updatedAt = updatedAt?.toEpochMilliseconds(),
        isDone = isDone
    )
}
