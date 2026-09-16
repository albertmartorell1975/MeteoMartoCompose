package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

/**
 * A standard layout wrapper for all feature screens in MeteoMarto.
 *
 * It ensures consistency by:
 * 1. Providing a [Surface] with the correct theme background and content colors.
 * 2. Applying the root [modifier] (usually fillMaxSize).
 * 3. Standardizing how overlays or screens are contained.
 *
 * Use this instead of manual Surface/Box combinations at the root of your screens.
 *
 * @param modifier Applied to the root Surface.
 * @param content The screen content.
 */
@Composable
fun MmScaffold(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MeteoMartoTheme.colors.surface,
        contentColor = MeteoMartoTheme.colors.onSurface
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}
