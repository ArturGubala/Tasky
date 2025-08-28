package com.example.tasky.agenda.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.tasky.agenda.presentation.agenda_list.AgendaScreenRoot
import com.example.tasky.auth.presentation.navigation.navigateToLoginScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.agendaNavGraph(
    navController: NavHostController
) {
    navigation<AgendaGraph>(
        startDestination = AgendaScreen,
    ) {
        agendaScreen(navController = navController)
    }
}

fun NavController.navigateToAgendaScreen() = navigate(AgendaScreen)

fun NavGraphBuilder.agendaScreen(
    navController: NavController
) {
    composable<AgendaScreen> {
        AgendaScreenRoot(
            onSuccessfulLogout = { navController.navigateToLoginScreen() }
        )
    }
}

@Serializable
object AgendaGraph

@Serializable
object AgendaScreen
