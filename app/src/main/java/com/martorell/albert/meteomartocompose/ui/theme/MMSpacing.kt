package com.martorell.albert.meteomartocompose.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * MMSpacing defines the spacing scale for the MeteoMarto Design System.
 * It uses a consistent increment to ensure visual harmony across the app.
 */
@Immutable
data class MMSpacing(
    val none: Dp = 0.dp,
    val xs: Dp = 4.dp,
    val s: Dp = 8.dp,
    val m: Dp = 16.dp,
    val l: Dp = 24.dp,
    val xl: Dp = 32.dp,
)

/**
 * CompositionLocal key for accessing MMSpacing tokens.
 * We use staticCompositionLocalOf because spacing tokens are unlikely to change
 * during the app's lifecycle, which optimizes performance.
 */
val LocalMMSpacing = staticCompositionLocalOf { MMSpacing() }