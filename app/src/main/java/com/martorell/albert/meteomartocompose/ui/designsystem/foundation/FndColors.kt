package com.martorell.albert.meteomartocompose.ui.designsystem.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * FndColors defines the semantic color roles for the MeteoMarto Design System.
 */
@Immutable
data class FndColors(
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

val FndColorsLight = FndColors(
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

val FndColorsDark = FndColors(
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

val LocalFndColors = compositionLocalOf { FndColorsLight }