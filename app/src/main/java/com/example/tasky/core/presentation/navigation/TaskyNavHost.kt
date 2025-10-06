package com.example.tasky.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.tasky.agenda.presentation.navigation.AgendaGraph
import com.example.tasky.agenda.presentation.navigation.agendaNavGraph
import com.example.tasky.auth.presentation.navigation.AuthGraph
import com.example.tasky.auth.presentation.navigation.authNavGraph

@Composable
fun TaskyNavHost(
    navController: NavHostController,
    isLoggedIn: Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) AgendaGraph else AuthGraph
    ) {
        authNavGraph(
            navController = navController,
            isLoggedIn = isLoggedIn
        )
        agendaNavGraph(navController = navController)
    }
}
