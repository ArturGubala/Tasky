package com.example.tasky.core.presentation.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalExtendedColors = staticCompositionLocalOf { lightExtendedColors }

val ColorScheme.extended: ExtendedColors
    @ReadOnlyComposable
    @Composable
    get() = LocalExtendedColors.current

@Immutable
data class ExtendedColors(
    val backgroundOpacity50: Color,
    val surfaceHigher: Color,
    val surfaceHigherOpacity60: Color,
    val onSurfaceVariantOpacity70: Color,
    val success: Color,
    val link: Color,
    val secondaryOpacity80: Color,
    val tertiaryOpacity80: Color,
    val supplementary: Color
)
