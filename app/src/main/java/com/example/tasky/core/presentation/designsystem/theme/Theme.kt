package com.example.tasky.core.presentation.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
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

private val DarkColorScheme = darkColorScheme(
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

val ColorScheme.onSurfaceVariantOpacity70: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Dark.OnSurfaceVariantOpacity70 else Light.OnSurfaceVariantOpacity70

val ColorScheme.success: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Dark.Success else Light.Success

val ColorScheme.link: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Dark.Link else Light.Link

val ColorScheme.backgroundOpacity50: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Dark.BackgroundOpacity50 else Light.BackgroundOpacity50

val ColorScheme.surfaceHigher: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Dark.SurfaceHigher else Light.SurfaceHigher

val ColorScheme.surfaceHigherOpacity60: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Dark.SurfaceHigherOpacity60 else Light.SurfaceHigherOpacity60

val ColorScheme.secondaryOpacity80: Color
    get() = Brand.SecondaryOpacity80

val ColorScheme.tertiaryOpacity80: Color
    get() = Brand.TertiaryOpacity80

val ColorScheme.supplementary: Color
    get() = Brand.Supplementary

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
