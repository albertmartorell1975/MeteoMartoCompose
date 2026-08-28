package com.martorell.albert.meteomartocompose.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * MMColors defines the semantic color roles for the MeteoMarto Design System.
 * These roles map to Material 3 Expressive palette concepts while allowing
 * for brand-specific customizations (e.g., Success states for weather updates).
 */
@Immutable
data class MMColors(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val surface: Color,
    val onSurface: Color,
    val error: Color,
    val onError: Color,
    val success: Color,
    val onSuccess: Color,
    val isLight: Boolean
)

val MMColorsLight = MMColors(
    primary = Color(0xFF6650a4),
    onPrimary = Color.White,
    secondary = Color(0xFF625b71),
    onSecondary = Color.White,
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
    error = Color(0xFFB3261E),
    onError = Color.White,
    success = Color(0xFF2E7D32),
    onSuccess = Color.White,
    isLight = true
)

val MMColorsDark = MMColors(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    success = Color(0xFF81C784),
    onSuccess = Color(0xFF003300),
    isLight = false
)

/**
 * CompositionLocal key for accessing MMColors tokens.
 * We use compositionLocalOf because colors change frequently (Light/Dark mode),
 * which ensures only the components using the specific colors are recomposed.
 */
val LocalMMColors = compositionLocalOf { MMColorsLight }