package com.example.tasky.agenda.data.repository

import com.example.tasky.agenda.domain.data.TaskRepository
import com.example.tasky.agenda.domain.data.network.TaskRemoteDataSource
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.data.database.task.dao.TaskPendingSyncDao
import com.example.tasky.core.data.database.task.mappers.toTask
import com.example.tasky.core.domain.data.TaskLocalDataStore
import com.example.tasky.core.domain.datastore.SessionStorage
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.domain.util.asEmptyDataResult
import com.example.tasky.core.domain.util.onError
import com.example.tasky.core.domain.util.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfflineFirstTaskRepository(
    private val taskRemoteDataSource: TaskRemoteDataSource,
    private val taskLocalDataStore: TaskLocalDataStore,
    private val applicationScope: CoroutineScope,
    private val taskPendingSyncDao: TaskPendingSyncDao,
    private val sessionStorage: SessionStorage,
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

    override suspend fun syncPendingTask() {
        withContext(Dispatchers.IO) {
            val userId = sessionStorage.get()?.userId ?: return@withContext

            val createdTasks = async {
                taskPendingSyncDao.getAllTaskPendingSyncEntities(userId)
            }
            val deletedTasks = async {
                taskPendingSyncDao.getAllDeletedTaskSyncEntities(userId)
            }

            val createJobs = createdTasks
                .await()
                .map {
                    launch {
                        val task = it.task.toTask()
                        when (it.operation) {
                            SyncOperation.CREATE -> {
                                taskRemoteDataSource.createTask(task)
                                    .onError { }
                                    .onSuccess {
                                        applicationScope.launch {
                                            taskPendingSyncDao.deleteTaskPendingSyncEntity(it.id)
                                        }.join()
                                    }
                            }

                            SyncOperation.UPDATE -> {
                                taskRemoteDataSource.updateTask(task)
                                    .onError { }
                                    .onSuccess {
                                        applicationScope.launch {
                                            taskPendingSyncDao.deleteTaskPendingSyncEntity(it.id)
                                        }.join()
                                    }
                            }
                        }
                    }
                }
            val deleteJobs = deletedTasks
                .await()
                .map {
                    launch {
                        taskRemoteDataSource.deleteTask(it.taskId)
                            .onError { }
                            .onSuccess { result ->
                                applicationScope.launch {
                                    taskPendingSyncDao.deleteDeletedTaskSyncEntity(it.taskId)
                                }.join()
                            }
                    }
                }

            createJobs.forEach { it.join() }
            deleteJobs.forEach { it.join() }
        }
    }
}
