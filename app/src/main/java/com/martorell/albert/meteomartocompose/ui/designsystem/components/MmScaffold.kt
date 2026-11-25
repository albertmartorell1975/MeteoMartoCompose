package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

/**
 * Stateless themed surface wrapper establishing the baseline background and foreground content
 * colors for screen layouts while leaving sizing constraints to the caller.
 *
 * @param modifier Modifier applied to the root `Surface`.
 * @param containerColor Background color of the surface container.
 * @param contentColor Preferred foreground content color propagated to descendants.
 * @param content Composable content rendered inside an unpadded `Box`.
 */
@Composable
fun MmScaffold(
    modifier: Modifier = Modifier,
    containerColor: Color = MeteoMartoTheme.colors.background,
    contentColor: Color = MeteoMartoTheme.colors.onSurface,
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Box {
            content()
        }
    }
}

@MmPreview
@Composable
private fun MmScaffoldPreview() {
    MeteoMartoTheme {
        MmScaffold(modifier = Modifier.fillMaxWidth()) {
            MmCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MeteoMartoTheme.spacing.medium),
            ) {
                MmText.TitleMedium(text = "Sabadell Forecast")
                MmText.BodyMedium(
                    text = "Card container resting on scaffold background",
                    color = MeteoMartoTheme.colors.onSurfaceVariant,
                )
            }
        }
    }
}
