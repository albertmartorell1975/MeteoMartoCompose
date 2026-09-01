package com.martorell.albert.meteomartocompose.ui.components.designsystem

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.martorell.albert.meteomartocompose.ui.theme.MMTypography
import com.martorell.albert.meteomartocompose.ui.theme.MeteoMartoTheme

/**
 * [MMText] is the mandatory typographic boundary for the MeteoMarto Design System.
 * 
 * Following Architecture Audit v2 (Point 2), feature screens must use this component
 * instead of Material 3's Text to ensure strict adherence to the [MMTypography] tokens
 * and the Roboto Flex variable font configuration.
 */
@Composable
fun MMText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
    )
}

/**
 * Convenience functions for semantic roles.
 */
object MMText {
    @Composable
    fun DisplayLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
    ) = MMText(text, MeteoMartoTheme.typography.displayLarge, modifier, color, textAlign)

    @Composable
    fun HeadlineMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
    ) = MMText(text, MeteoMartoTheme.typography.headlineMedium, modifier, color, textAlign)

    @Composable
    fun BodyLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
    ) = MMText(text, MeteoMartoTheme.typography.bodyLarge, modifier, color, textAlign)
    
    @Composable
    fun BodyMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
    ) = MMText(text, MeteoMartoTheme.typography.bodyMedium, modifier, color, textAlign)

    @Composable
    fun LabelSmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
    ) = MMText(text, MeteoMartoTheme.typography.labelSmall, modifier, color, textAlign)
}

// --- Previews ---

@MMPreview
@Composable
private fun MMTextPreview() {
    MeteoMartoTheme {
        androidx.compose.foundation.layout.Column {
            MMText.DisplayLarge("Display Large")
            MMText.HeadlineMedium("Headline Medium")
            MMText.BodyLarge("Body Large")
            MMText.BodyMedium("Body Medium")
            MMText.LabelSmall("Label Small")
        }
    }
}
