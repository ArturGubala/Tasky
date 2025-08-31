package com.example.tasky.agenda.presentation.agenda_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.agenda.presentation.util.AgendaItemType
import com.example.tasky.agenda.presentation.util.AgendaTypeConfig
import com.example.tasky.agenda.presentation.util.AgendaTypeConfigProvider
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AgendaDetailScreenRoot(
    agendaItemType: AgendaItemType,
    agendaDetailView: AgendaDetailView,
    agendaId: String = "",
    onBackClick: () -> Unit,
    viewModel: AgendaDetailViewModel = koinViewModel(
        parameters = { parametersOf(agendaId) }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val agendaItemTypeConfiguration by remember(agendaItemType) {
        mutableStateOf(AgendaTypeConfigProvider.getConfig(type = agendaItemType))
    }

    AgendaDetailScreen(
        state = state,
        agendaDetailView = agendaDetailView,
        agendaItemTypeConfiguration = agendaItemTypeConfiguration
    )
}

@Composable
fun AgendaDetailScreen(
    state: AgendaDetailState,
    agendaDetailView: AgendaDetailView,
    agendaItemTypeConfiguration: AgendaTypeConfig
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = agendaItemTypeConfiguration.displayName)
    }
}
