package com.example.tasky.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.tasky.agenda.domain.util.AgendaKind
import com.example.tasky.agenda.presentation.navigation.AgendaGraph
import com.example.tasky.agenda.presentation.navigation.agendaNavGraph
import com.example.tasky.agenda.presentation.navigation.navigateToAgendaDetailScreen
import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.auth.presentation.navigation.AuthGraph
import com.example.tasky.auth.presentation.navigation.authNavGraph

@Composable
fun TaskyNavHost(
    navController: NavHostController,
    isLoggedIn: Boolean,
    agendaItemId: String? = null,
    agendaKind: AgendaKind? = null,
) {
    LaunchedEffect(agendaItemId, agendaKind, isLoggedIn) {
        if (agendaItemId != null && agendaKind != null && isLoggedIn) {
            // Small delay to ensure nav graph is ready
            // Added by ai, I'm not sure about that
            kotlinx.coroutines.delay(200)

            navController.navigateToAgendaDetailScreen(
                agendaKind = agendaKind,
                agendaDetailView = AgendaDetailView.EDIT,
                agendaId = agendaItemId
            )
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) AgendaGraph else AuthGraph
    ) {
        authNavGraph(
            navController = navController,
            isLoggedIn = isLoggedIn
        )
        agendaNavGraph(navController = navController)
        // Is it possible to navigate from here?
//        if(agendaItemId != null && agendaKind != null) {
//            navController.navigateToAgendaDetailScreen(
//                agendaKind = agendaKind,
//                agendaDetailView = AgendaDetailView.EDIT,
//                agendaId = agendaItemId
//            )
//        }
    }
}
