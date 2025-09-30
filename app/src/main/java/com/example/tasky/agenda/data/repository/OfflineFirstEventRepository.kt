package com.example.tasky.agenda.data.repository

import com.example.tasky.agenda.data.network.event.dto.ConfirmUploadRequest
import com.example.tasky.agenda.domain.data.EventRepository
import com.example.tasky.agenda.domain.data.network.EventRemoteDataSource
import com.example.tasky.agenda.domain.data.sync.SyncAgendaItemScheduler
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.LookupAttendee
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.data.database.event.dao.EventPendingSyncDao
import com.example.tasky.core.data.database.event.mappers.toEvent
import com.example.tasky.core.domain.data.EventLocalDataSource
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
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfflineFirstEventRepository(
    private val eventRemoteDataSource: EventRemoteDataSource,
    private val eventLocalDataSource: EventLocalDataSource,
    private val applicationScope: CoroutineScope,
    private val eventPendingSyncDao: EventPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val syncAgendaItemScheduler: SyncAgendaItemScheduler,
) : EventRepository {

    override suspend fun upsertEvent(
        event: Event,
        syncOperation: SyncOperation,
    ): EmptyResult<DataError> {
        val localResult = eventLocalDataSource.upsertEvent(event)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }

        when (syncOperation) {
            SyncOperation.CREATE -> {
                return eventRemoteDataSource.createEvent(event)
                    .onError { error ->
                        applicationScope.launch {
                            syncAgendaItemScheduler.scheduleSync(
                                type = SyncAgendaItemScheduler.SyncType.UpsertAgendaItem(
                                    item = AgendaItem.Event(id = event.id),
                                    operation = syncOperation
                                )
                            )
                        }.join()
                    }
                    .onSuccess { eventResponse ->
                        var uploadedKeys: List<String> = listOf()

                        // UPLOAD TO AWS
                        val uploaded = coroutineScope {
                            eventResponse.uploadUrls.map { uploadUrl ->
                                async {
                                    val bytes =
                                        event.photos.first { it.id == uploadUrl.photoKey }.compressedBytes
                                    if (bytes != null) {
                                        when (eventRemoteDataSource.uploadPhoto(
                                            url = uploadUrl.url,
                                            photo = bytes
                                        )) {
                                            is Result.Success -> uploadUrl.uploadKey
                                            is Result.Error -> null
                                        }
                                    } else {
                                        null
                                    }
                                }
                            }.awaitAll().filterNotNull()
                        }

                        uploadedKeys = uploadedKeys + uploaded

                        // CONFIRM
                        applicationScope.launch {
                            eventRemoteDataSource.confirmUpload(
                                eventId = event.id,
                                confirmUploadRequest = ConfirmUploadRequest(uploadedKeys = uploadedKeys)
                            )
                                .onSuccess { event ->
                                    eventLocalDataSource.upsertEvent(event)
                                }
                        }.join()
                    }.asEmptyDataResult()
            }

            SyncOperation.UPDATE -> {
                return eventRemoteDataSource.updateEvent(event)
                    .onError { error ->
                        applicationScope.launch {
                            syncAgendaItemScheduler.scheduleSync(
                                type = SyncAgendaItemScheduler.SyncType.UpsertAgendaItem(
                                    item = AgendaItem.Event(id = event.id),
                                    operation = syncOperation
                                )
                            )
                        }.join()
                    }
                    .onSuccess { eventResponse ->
                        var uploadedKeys: List<String> = listOf()

                        // UPLOAD TO AWS
                        val uploaded = coroutineScope {
                            eventResponse.uploadUrls.map { uploadUrl ->
                                async {
                                    val bytes =
                                        event.photos.first { it.id == uploadUrl.photoKey }.compressedBytes
                                    if (bytes != null) {
                                        when (eventRemoteDataSource.uploadPhoto(
                                            url = uploadUrl.url,
                                            photo = bytes
                                        )) {
                                            is Result.Success -> uploadUrl.uploadKey
                                            is Result.Error -> null
                                        }
                                    } else {
                                        null
                                    }
                                }
                            }.awaitAll().filterNotNull()
                        }

                        uploadedKeys = uploadedKeys + uploaded

                        // CONFIRM
                        applicationScope.launch {
                            eventRemoteDataSource.confirmUpload(
                                eventId = event.id,
                                confirmUploadRequest = ConfirmUploadRequest(uploadedKeys = uploadedKeys)
                            )
                                .onSuccess { event ->
                                    eventLocalDataSource.upsertEvent(event)
                                }
                        }.join()
                    }.asEmptyDataResult()
            }
        }
    }

    override suspend fun deleteEvent(id: String): EmptyResult<DataError> {
        eventLocalDataSource.deleteEvent(id = id)

        return eventRemoteDataSource.deleteEvent(id = id)
            .onError { error ->
                when (error) {
                    DataError.Network.NotFound() -> {}
                    else -> {
                        applicationScope.launch {
                            syncAgendaItemScheduler.scheduleSync(
                                type = SyncAgendaItemScheduler.SyncType.DeleteAgendaItem(
                                    item = AgendaItem.Event(id = id)
                                )
                            )
                        }.join()
                    }
                }
            }.asEmptyDataResult()
    }

    override suspend fun getAttendee(email: String): Result<LookupAttendee, DataError.Network> {
        return eventRemoteDataSource.getAttendee(email = email)
    }

    override suspend fun deleteAttendee(userId: String, eventId: String) {
        eventLocalDataSource.deleteAttendee(userId = userId, eventId = eventId)
    }

    override suspend fun syncPendingEvent() {
        withContext(Dispatchers.IO) {
            val userId = sessionStorage.get()?.userId ?: return@withContext

            val createdEvents = async {
                eventPendingSyncDao.getAllEventPendingSyncEntities(userId)
            }
            val deletedEvents = async {
                eventPendingSyncDao.getAllDeletedEventSyncEntities(userId)
            }

            val createJobs = createdEvents
                .await()
                .map { it ->
                    launch {
                        val event = it.event.toEvent()
                        when (it.operation) {
                            SyncOperation.CREATE -> {
                                eventRemoteDataSource.createEvent(event)
                                    .onSuccess {
                                        applicationScope.launch {
                                            eventPendingSyncDao.deleteEventPendingSyncEntity(
                                                eventId = it.event.id,
                                                operations = listOf(SyncOperation.CREATE)
                                            )
                                        }.join()
                                    }
                            }

                            SyncOperation.UPDATE -> {
                                eventRemoteDataSource.updateEvent(event)
                                    .onSuccess {
                                        applicationScope.launch {
                                            eventPendingSyncDao.deleteEventPendingSyncEntity(
                                                eventId = it.event.id,
                                                operations = listOf(SyncOperation.UPDATE)
                                            )
                                        }.join()
                                    }
                            }
                        }
                    }
                }
            val deleteJobs = deletedEvents
                .await()
                .map {
                    launch {
                        eventRemoteDataSource.deleteEvent(it.eventId)
                            .onSuccess { result ->
                                applicationScope.launch {
                                    eventPendingSyncDao.deleteDeletedEventSyncEntity(it.eventId)
                                }.join()
                            }
                    }
                }

            createJobs.forEach { it.join() }
            deleteJobs.forEach { it.join() }
        }
    }


}
