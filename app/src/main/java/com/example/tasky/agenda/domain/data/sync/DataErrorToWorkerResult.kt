package com.example.tasky.agenda.domain.data.sync

import androidx.work.ListenableWorker
import com.example.tasky.core.domain.util.DataError

fun DataError.toWorkerResult(): ListenableWorker.Result {
    return when (this) {
        DataError.Local.DISK_FULL -> ListenableWorker.Result.failure()
        DataError.Network.UNAUTHORIZED -> ListenableWorker.Result.retry()
        DataError.Network.CONFLICT -> ListenableWorker.Result.retry()
        DataError.Network.TOO_MANY_REQUESTS -> ListenableWorker.Result.retry()
        DataError.Network.NO_INTERNET -> ListenableWorker.Result.retry()
        DataError.Network.SERVER_ERROR -> ListenableWorker.Result.retry()
        DataError.Network.SERIALIZATION -> ListenableWorker.Result.failure()
        DataError.Network.UNKNOWN -> ListenableWorker.Result.failure()
        DataError.Network.FORBIDDEN -> ListenableWorker.Result.failure()
        DataError.Network.NOT_FOUND -> ListenableWorker.Result.failure()
        DataError.Network.BAD_REQUEST -> ListenableWorker.Result.failure()
        else -> ListenableWorker.Result.failure()
    }
}
