package com.example.tasky.agenta.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
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
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Agenda sccreen")
        }
    }
}

@Serializable
object AgendaGraph

@Serializable
object AgendaScreen
