package com.example.tasky.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.tasky.agenda.presentation.navigation.navigateToAgendaListScreen
import com.example.tasky.auth.presentation.login.LoginScreenRoot
import com.example.tasky.auth.presentation.register.RegisterScreenRoot
import kotlinx.serialization.Serializable

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    isLoggedIn: Boolean
) {
    navigation<AuthGraph>(
        startDestination = if (isLoggedIn) LoginScreen else RegisterScreen
    ) {
        registerScreen(navController = navController)
        loginScreen(navController = navController)
    }
}

fun NavController.navigateToRegisterScreen() = navigate(RegisterScreen) {
    popUpTo(LoginScreen) {
        inclusive = true
        saveState = true
    }
    restoreState = true
    launchSingleTop = true
}

fun NavGraphBuilder.registerScreen(
    navController: NavController
) {
    composable<RegisterScreen> {
        RegisterScreenRoot(
            onLoginLinkClick = { navController.navigateToLoginScreen() },
            onSuccessfulRegistration = { navController.navigateToLoginScreen() }
        )
    }
}

fun NavController.navigateToLoginScreen() = navigate(LoginScreen) {
    popUpTo(RegisterScreen) {
        inclusive = true
        saveState = true
    }
    restoreState = true
    launchSingleTop = true
}

fun NavGraphBuilder.loginScreen(
    navController: NavController
) {
    composable<LoginScreen> {
        LoginScreenRoot(
            onRegisterLinkClick = { navController.navigateToRegisterScreen() },
            onSuccessfulLogin = { navController.navigateToAgendaListScreen() }
        )
    }
}

@Serializable
object AuthGraph

@Serializable
object RegisterScreen

@Serializable
object LoginScreen
