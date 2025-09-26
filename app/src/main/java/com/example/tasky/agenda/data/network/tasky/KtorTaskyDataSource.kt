package com.example.tasky.agenda.data.network.tasky

import com.example.tasky.agenda.data.network.tasky.dto.AgendaDto
import com.example.tasky.agenda.data.network.tasky.mappers.toAgenda
import com.example.tasky.agenda.domain.data.network.TaskyRemoteDataSource
import com.example.tasky.agenda.domain.model.Agenda
import com.example.tasky.core.data.networking.get
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.domain.util.map
import io.ktor.client.HttpClient

class KtorTaskyDataSource(
    private val httpClient: HttpClient,
) : TaskyRemoteDataSource {

    override suspend fun getFullAgenda(): Result<Agenda, DataError.Network> {
        return httpClient.get<AgendaDto>(
            route = "/fullAgenda",
        ).map { it.toAgenda() }
    }
}
