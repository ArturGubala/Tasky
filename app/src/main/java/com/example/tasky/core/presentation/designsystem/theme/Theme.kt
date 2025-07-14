package com.example.tasky.core.presentation.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Light.Primary,
    secondary = Brand.Secondary,
    tertiary = Brand.Tertiary,
    onPrimary = Light.OnPrimary,
    background = Light.Background,
    onBackground = Light.OnBackground,
    surface = Light.Surface,
    onSurface = Light.OnSurface,
    onSurfaceVariant = Light.OnSurfaceVariant,
    error = Light.Error,
    outline = Light.Outline
)

private val LightColorScheme = lightColorScheme(
    primary = Dark.Primary,
    secondary = Brand.Secondary,
    tertiary = Brand.Tertiary,
    onPrimary = Dark.OnPrimary,
    background = Dark.Background,
    onBackground = Dark.OnBackground,
    surface = Dark.Surface,
    onSurface = Dark.OnSurface,
    onSurfaceVariant = Dark.OnSurfaceVariant,
    error = Dark.Error,
    outline = Dark.Outline
)

@Composable
fun TaskyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
