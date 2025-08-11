package com.example.tasky.auth.presentation.navigation

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
import com.example.tasky.auth.presentation.register.RegisterScreenRoot
import kotlinx.serialization.Serializable

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    isNewUser: Boolean
) {
    navigation<AuthRoute>(
        startDestination = if (isNewUser) RegisterScreen else LoginScreen
    ) {
        registerScreen(navController = navController)
        loginScreen(navController = navController)
    }
}

fun NavController.navigateToRegisterScreen() = navigate(RegisterScreen)

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

fun NavController.navigateToLoginScreen() = navigate(LoginScreen)

fun NavGraphBuilder.loginScreen(
    navController: NavController
) {
    composable<LoginScreen> {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login sccreen")
        }
    }
}

@Serializable
object AuthRoute

@Serializable
object RegisterScreen

@Serializable
object LoginScreen
