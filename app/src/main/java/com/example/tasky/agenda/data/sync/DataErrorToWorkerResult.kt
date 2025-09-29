package com.example.tasky.agenda.data.sync

import androidx.work.ListenableWorker
import com.example.tasky.core.domain.util.DataError

fun DataError.toWorkerResult(): ListenableWorker.Result {
    return when (this) {
        DataError.Local.DiskFull -> ListenableWorker.Result.failure()
        DataError.Network.Unauthorized -> ListenableWorker.Result.retry()
        DataError.Network.Conflict() -> ListenableWorker.Result.retry()
        DataError.Network.TooManyRequests -> ListenableWorker.Result.retry()
        DataError.Network.NoInternet -> ListenableWorker.Result.retry()
        DataError.Network.ServerError -> ListenableWorker.Result.retry()
        DataError.Network.Serialization -> ListenableWorker.Result.failure()
        DataError.Network.Unknown -> ListenableWorker.Result.failure()
        DataError.Network.Forbidden -> ListenableWorker.Result.failure()
        DataError.Network.NotFound() -> ListenableWorker.Result.failure()
        DataError.Network.BadRequest() -> ListenableWorker.Result.failure()
        else -> ListenableWorker.Result.failure()
    }
}
