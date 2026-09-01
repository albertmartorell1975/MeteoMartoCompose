package com.martorell.albert.meteomartocompose.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.martorell.albert.meteomartocompose.data.preferences.UserPreferences

/**
 * MMDensityProvider handles dynamic scaling for accessibility.
 *
 * **Architectural Decision (ADR 07)**:
 * This provider implements a "Global UI Zoom" strategy by overriding [LocalDensity].
 *
 * It distinguishes between two semantic concepts:
 * 1. **Layout Density (dp)**: Structural scaling. By multiplying [Density.density], we scale the
 *    entire UI (margins, paddings, sizes). This creates the "Pinch-to-zoom" effect.
 * 2. **Font Scaling (sp)**: Textual scaling. By multiplying [Density.fontScale], we ensure text
 *    grows proportionally with the UI.
 *
 * **CAUTION**: Modifying structural density affects dp-to-px conversions. Ensure all components
 * adhere to the 48dp touch target standard to remain safe during scaling.
 *
 * @param userScale The scaling factor from [UserPreferences] (default 1.0f).
 * @param content The UI tree to be rendered within this density context.
 */
@Composable
fun MMDensityProvider(
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
