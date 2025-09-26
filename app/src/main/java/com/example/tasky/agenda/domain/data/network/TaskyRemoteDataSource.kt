package com.example.tasky.agenda.domain.data.network

import com.example.tasky.agenda.domain.model.Agenda
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.Result

interface TaskyRemoteDataSource {
    suspend fun getFullAgenda(): Result<Agenda, DataError.Network>
}
