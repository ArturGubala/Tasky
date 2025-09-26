package com.example.tasky.agenda.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tasky.agenda.domain.data.TaskyRepository
import com.example.tasky.core.domain.util.Result.Error as ResultError
import com.example.tasky.core.domain.util.Result.Success as ResultSuccess

class FetchAgendaItemsWorker(
    context: Context,
    params: WorkerParameters,
    private val taskyRepository: TaskyRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        return when (val result = taskyRepository.fetchFullAgenda()) {
            is ResultError -> {
                result.error.toWorkerResult()
            }

            is ResultSuccess -> Result.success()
        }
    }
}
