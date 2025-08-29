package com.example.tasky.agenda.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.tasky.agenda.presentation.agenda_detail.AgendaDetailScreenRoot
import com.example.tasky.agenda.presentation.agenda_list.AgendaScreenRoot
import com.example.tasky.agenda.presentation.util.AgendaMode
import com.example.tasky.agenda.presentation.util.AgendaType
import com.example.tasky.auth.presentation.navigation.navigateToLoginScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.agendaNavGraph(
    navController: NavHostController
) {
    navigation<AgendaGraph>(
        startDestination = AgendaListScreen,
    ) {
        agendaListScreen(navController = navController)
        agendaDetailScreen(navController = navController)
    }
}

fun NavController.navigateToAgendaListScreen() = navigate(AgendaListScreen)

fun NavGraphBuilder.agendaListScreen(
    navController: NavController
) {
    composable<AgendaListScreen> {
        AgendaScreenRoot(
            onSuccessfulLogout = { navController.navigateToLoginScreen() },
            onFabMenuOptionClick = { agendaType, agendaMode, agendaId ->
                navController.navigateToAgendaDetailScreen(agendaType, agendaMode, agendaId)
            }
        )
    }
}

fun NavController.navigateToAgendaDetailScreen(agendaType: AgendaType,
                                               agendaMode: AgendaMode,
                                               agendaId: String)
= navigate(AgendaDetailScreen(
    agendaType = agendaType,
    agendaMode = agendaMode,
    agendaId = agendaId
))

fun NavGraphBuilder.agendaDetailScreen(
    navController: NavController
) {
    composable<AgendaDetailScreen> {
        val args = it.toRoute<AgendaDetailScreen>()
        AgendaDetailScreenRoot(
            agendaType = args.agendaType,
            agendaMode = args.agendaMode,
            agendaId = args.agendaId,
            onBackClick = {}
        )
    }
}

@Serializable
object AgendaGraph

@Serializable
object AgendaListScreen

@Serializable
data class AgendaDetailScreen(
    val agendaType: AgendaType,
    val agendaMode: AgendaMode,
    val agendaId: String = ""
)
