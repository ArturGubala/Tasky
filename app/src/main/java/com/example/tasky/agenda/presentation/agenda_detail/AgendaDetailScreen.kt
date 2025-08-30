package com.example.tasky.agenda.presentation.agenda_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.agenda.presentation.util.AgendaItemType
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AgendaDetailScreenRoot(
    agendaItemType: AgendaItemType,
    agendaDetailView: AgendaDetailView,
    agendaId: String = "",
    onBackClick: () -> Unit,
    viewModel: AgendaDetailViewModel = koinViewModel(
        parameters = { parametersOf(agendaItemType, agendaDetailView, agendaId) }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AgendaDetailScreen(state = state)
}

@Composable
fun AgendaDetailScreen(state: AgendaDetailState) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = state.agendaDetailConfig.displayName)
    }
}
