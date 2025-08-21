package com.example.tasky.agenta.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.tasky.agenta.presentation.AgendaScreenRoot
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
        AgendaScreenRoot()
    }
}

@Serializable
object AgendaGraph

@Serializable
object AgendaScreen
