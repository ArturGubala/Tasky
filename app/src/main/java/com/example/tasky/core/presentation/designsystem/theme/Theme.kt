package com.example.tasky.core.presentation.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
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

val lightExtendedColors = ExtendedColors(
    backgroundOpacity50 = Light.BackgroundOpacity50,
    surfaceHigher = Light.SurfaceHigher,
    surfaceHigherOpacity60 = Light.SurfaceHigherOpacity60,
    onSurfaceVariantOpacity70 = Light.OnSurfaceVariantOpacity70,
    success = Light.Success,
    link = Light.Link,
    secondaryOpacity80 = Brand.SecondaryOpacity80,
    tertiaryOpacity80 = Brand.TertiaryOpacity80,
    supplementary = Brand.Supplementary
)

val darkExtendedColors = ExtendedColors(
    backgroundOpacity50 = Dark.BackgroundOpacity50,
    surfaceHigher = Dark.SurfaceHigher,
    surfaceHigherOpacity60 = Dark.SurfaceHigherOpacity60,
    onSurfaceVariantOpacity70 = Dark.OnSurfaceVariantOpacity70,
    success = Dark.Success,
    link = Dark.Link,
    secondaryOpacity80 = Brand.SecondaryOpacity80,
    tertiaryOpacity80 = Brand.TertiaryOpacity80,
    supplementary = Brand.Supplementary
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

    val extendedColors = if (darkTheme) darkExtendedColors else lightExtendedColors
    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
