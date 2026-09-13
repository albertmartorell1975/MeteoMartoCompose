package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

@Composable
fun MmText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    fontWeight: FontWeight? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        style = style.copy(
            fontWeight = fontWeight ?: style.fontWeight
        ),
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
    )
}

object MmText {
    @Composable
    fun DisplayLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontWeight: FontWeight? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE,
    ) = MmText(
        text,
        MeteoMartoTheme.typography.displayLarge,
        modifier,
        color,
        textAlign,
        fontWeight,
        overflow,
        maxLines
    )

    @Composable
    fun HeadlineMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontWeight: FontWeight? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE,
    ) = MmText(
        text,
        MeteoMartoTheme.typography.headlineMedium,
        modifier,
        color,
        textAlign,
        fontWeight,
        overflow,
        maxLines
    )

    @Composable
    fun TitleLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontWeight: FontWeight? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE,
    ) = MmText(
        text,
        MeteoMartoTheme.typography.titleLarge,
        modifier,
        color,
        textAlign,
        fontWeight,
        overflow,
        maxLines
    )

    @Composable
    fun TitleMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontWeight: FontWeight? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE,
    ) = MmText(
        text,
        MeteoMartoTheme.typography.titleMedium,
        modifier,
        color,
        textAlign,
        fontWeight,
        overflow,
        maxLines
    )

    @Composable
    fun BodyLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontWeight: FontWeight? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE,
    ) = MmText(
        text,
        MeteoMartoTheme.typography.bodyLarge,
        modifier,
        color,
        textAlign,
        fontWeight,
        overflow,
        maxLines
    )

    @Composable
    fun BodyMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontWeight: FontWeight? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE,
    ) = MmText(
        text,
        MeteoMartoTheme.typography.bodyMedium,
        modifier,
        color,
        textAlign,
        fontWeight,
        overflow,
        maxLines
    )

    @Composable
    fun LabelSmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontWeight: FontWeight? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE,
    ) = MmText(
        text,
        MeteoMartoTheme.typography.labelSmall,
        modifier,
        color,
        textAlign,
        fontWeight,
        overflow,
        maxLines
    )
}

@MmPreview
@Composable
private fun MmTextPreview() {
    MeteoMartoTheme {
        androidx.compose.foundation.layout.Column {
            MmText.DisplayLarge("Display Large")
            MmText.HeadlineMedium("Headline Medium")
            MmText.BodyLarge("Body Large")
            MmText.BodyMedium("Body Medium")
            MmText.LabelSmall("Label Small")
        }
    }
}
