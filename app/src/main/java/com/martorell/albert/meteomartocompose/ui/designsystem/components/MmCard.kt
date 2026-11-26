package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

/**
 * Stateless tonal card container using the MeteoMarto card shape, surface palette,
 * and subtle outline-variant border.
 *
 * @param modifier Modifier applied to the card root.
 * @param shape Corner shape of the card container.
 * @param containerColor Background color of the card surface.
 * @param contentColor Preferred foreground color for descendants inside the card.
 * @param border Optional border stroke drawn around the card perimeter.
 * @param contentPadding Inner padding applied around [content].
 * @param content Vertical column content rendered inside the card.
 */
@Composable
fun MmCard(
    modifier: Modifier = Modifier,
    shape: Shape = MeteoMartoTheme.shapes.card,
    containerColor: Color = MeteoMartoTheme.colors.surface,
    contentColor: Color = MeteoMartoTheme.colors.onSurface,
    border: BorderStroke? = BorderStroke(
        width = MeteoMartoTheme.dimensions.borderStrokeWidth,
        color = MeteoMartoTheme.colors.outlineVariant,
    ),
    contentPadding: PaddingValues = PaddingValues(MeteoMartoTheme.spacing.medium),
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        border = border,
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content,
        )
    }
}

/**
 * Interactive stateless tonal card container that enforces minimum touch target sizing
 * when clickable.
 *
 * @param onClick Callback invoked when the card is clicked.
 * @param modifier Modifier applied to the card root.
 * @param enabled Whether click interactions are enabled.
 * @param shape Corner shape of the card container.
 * @param containerColor Background color of the card surface.
 * @param contentColor Preferred foreground color for descendants inside the card.
 * @param border Optional border stroke drawn around the card perimeter.
 * @param contentPadding Inner padding applied around [content].
 * @param content Vertical column content rendered inside the card.
 */
@Composable
fun MmCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MeteoMartoTheme.shapes.card,
    containerColor: Color = MeteoMartoTheme.colors.surface,
    contentColor: Color = MeteoMartoTheme.colors.onSurface,
    border: BorderStroke? = BorderStroke(
        width = MeteoMartoTheme.dimensions.borderStrokeWidth,
        color = MeteoMartoTheme.colors.outlineVariant,
    ),
    contentPadding: PaddingValues = PaddingValues(MeteoMartoTheme.spacing.medium),
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier.minimumInteractiveComponentSize(),
        enabled = enabled,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        border = border,
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content,
        )
    }
}

@MmPreview
@Composable
private fun MmCardPreview() {
    MeteoMartoTheme {
        Surface(color = MeteoMartoTheme.colors.background) {
            Column(
                modifier = Modifier.padding(MeteoMartoTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MeteoMartoTheme.spacing.small),
            ) {
                MmCard(modifier = Modifier.fillMaxWidth()) {
                    MmText.TitleMedium(text = "Sabadell")
                    MmText.BodyMedium(
                        text = "Clear sky, 24°C",
                        color = MeteoMartoTheme.colors.onSurfaceVariant,
                    )
                }
                MmCard(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    MmText.TitleMedium(text = "Barcelona")
                    MmText.BodyMedium(
                        text = "Interactive weather card",
                        color = MeteoMartoTheme.colors.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@MmDynamicPalettesPreview
@Composable
private fun MmCardPalettesPreview() {
    MmDynamicPalettesShowcase {
        MmCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MeteoMartoTheme.spacing.medium),
        ) {
            MmText.TitleMedium(text = "Weather Summary")
            MmText.BodyMedium(
                text = "Surface tonal elevation on dynamic palette",
                color = MeteoMartoTheme.colors.onSurfaceVariant,
            )
        }
    }
}
