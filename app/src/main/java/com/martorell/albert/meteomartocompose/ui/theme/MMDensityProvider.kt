package com.martorell.albert.meteomartocompose.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.martorell.albert.meteomartocompose.data.preferences.UserPreferences

/**
 * MMDensityProvider handles dynamic screen density overrides for accessibility (pinch-to-zoom).
 * It intercepts the LocalDensity and applies a user-defined scale factor.
 *
 * @param userScale The scaling factor to apply (e.g., 1.2f for 20% increase).
 * @param content The composable content to be rendered with the custom density.
 */
@Composable
fun MMDensityProvider(
    userScale: Float = UserPreferences.DEFAULT_FONT_SCALE,
    content: @Composable () -> Unit
) {
    val currentDensity = LocalDensity.current
    
    val customDensity = Density(
        density = currentDensity.density * userScale,
        fontScale = currentDensity.fontScale * userScale
    )

    CompositionLocalProvider(LocalDensity provides customDensity) {
        content()
    }
}