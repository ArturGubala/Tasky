package com.example.tasky.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.tasky.agenta.presentation.navigation.AgendaRoute
import com.example.tasky.agenta.presentation.navigation.agendaNavGraph
import com.example.tasky.auth.presentation.navigation.AuthRoute
import com.example.tasky.auth.presentation.navigation.authNavGraph
import com.example.tasky.core.domain.AuthState

@Composable
fun TaskyNavHost(
    navController: NavHostController,
    authState: AuthState
) {
    NavHost(
        navController = navController,
        startDestination = when (authState) {
            AuthState.AUTHENTICATED -> AgendaRoute
            AuthState.NOT_AUTHENTICATED -> AuthRoute
            AuthState.TOKEN_EXPIRED -> AuthRoute
        }
    ) {
        authNavGraph(
            navController = navController,
            isNewUser = authState == AuthState.NOT_AUTHENTICATED
        )
        agendaNavGraph(navController = navController)
    }
}
