package com.martorell.albert.meteomartocompose.ui.designsystem.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.martorell.albert.meteomartocompose.data.preferences.UserPreferences

@Composable
fun FndDensityProvider(
    userScale: Float = UserPreferences.DEFAULT_FONT_SCALE,
    content: @Composable () -> Unit,
) {
    val currentDensity = LocalDensity.current
    
    val customDensity = Density(
        density = currentDensity.density * userScale,
        fontScale = currentDensity.fontScale * userScale,
    )

    CompositionLocalProvider(LocalDensity provides customDensity) {
        content()
    }
}
