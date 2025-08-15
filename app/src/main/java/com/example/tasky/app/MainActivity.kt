package com.example.tasky.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.ui.input.key.Key.Companion.Window
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.navigation.TaskyNavHost
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.state.value.isCheckingAuth }
        }
        enableEdgeToEdge(
            // TODO: is that hard-styled system bar color ok or should it be dynamic somehow?
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        // TODO: change to something that change color for system bars to transparent,
        // TODO: have to play with that
//        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets -> insets }
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()

            TaskyTheme {
                if(!state.isCheckingAuth) {
                    val navController = rememberNavController()
                    TaskyNavHost(
                        navController = navController,
                        isLoggedIn = state.isLoggedIn
                    )
                }
            }
        }
    }
}
