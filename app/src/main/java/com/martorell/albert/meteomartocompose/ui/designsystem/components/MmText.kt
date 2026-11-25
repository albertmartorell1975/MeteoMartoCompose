package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

/**
 * Base design-system typography composable wrapping Material 3 `Text` with optional weight
 * override while enforcing MeteoMarto typography tokens.
 *
 * @param text String content to display.
 * @param style Typographic `TextStyle` from [MeteoMartoTheme.typography].
 * @param modifier Modifier applied to the text layout.
 * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
 * @param textAlign Optional horizontal paragraph alignment.
 * @param fontWeight Optional weight override applied on top of [style].
 * @param overflow Overflow handling mode when text exceeds available bounds.
 * @param maxLines Maximum number of lines permitted before applying [overflow].
 */
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
            fontWeight = fontWeight ?: style.fontWeight,
        ),
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
    )
}

/**
 * Scoped typography scale helpers mapping each Material 3 type role from
 * [MeteoMartoTheme.typography] to [MmText].
 */
object MmText {
    /**
     * Renders [text] using the `displayLarge` typography token.
     *
     * @param text String content to display.
     * @param modifier Modifier applied to the text layout.
     * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
     * @param textAlign Optional horizontal paragraph alignment.
     * @param fontWeight Optional weight override.
     * @param overflow Overflow handling mode when text exceeds available bounds.
     * @param maxLines Maximum number of lines permitted before applying [overflow].
     */
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
        text = text,
        style = MeteoMartoTheme.typography.displayLarge,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
    )

    /**
     * Renders [text] using the `displayMedium` typography token.
     *
     * @param text String content to display.
     * @param modifier Modifier applied to the text layout.
     * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
     * @param textAlign Optional horizontal paragraph alignment.
     * @param fontWeight Optional weight override.
     * @param overflow Overflow handling mode when text exceeds available bounds.
     * @param maxLines Maximum number of lines permitted before applying [overflow].
     */
    @Composable
    fun DisplayMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontWeight: FontWeight? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE,
    ) = MmText(
        text = text,
        style = MeteoMartoTheme.typography.displayMedium,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
    )

    /**
     * Renders [text] using the `displaySmall` typography token.
     *
     * @param text String content to display.
     * @param modifier Modifier applied to the text layout.
     * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
     * @param textAlign Optional horizontal paragraph alignment.
     * @param fontWeight Optional weight override.
     * @param overflow Overflow handling mode when text exceeds available bounds.
     * @param maxLines Maximum number of lines permitted before applying [overflow].
     */
    @Composable
    fun DisplaySmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontWeight: FontWeight? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE,
    ) = MmText(
        text = text,
        style = MeteoMartoTheme.typography.displaySmall,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
    )

    /**
     * Renders [text] using the `headlineLarge` typography token.
     *
     * @param text String content to display.
     * @param modifier Modifier applied to the text layout.
     * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
     * @param textAlign Optional horizontal paragraph alignment.
     * @param fontWeight Optional weight override.
     * @param overflow Overflow handling mode when text exceeds available bounds.
     * @param maxLines Maximum number of lines permitted before applying [overflow].
     */
    @Composable
    fun HeadlineLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontWeight: FontWeight? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE,
    ) = MmText(
        text = text,
        style = MeteoMartoTheme.typography.headlineLarge,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
    )

    /**
     * Renders [text] using the `headlineMedium` typography token.
     *
     * @param text String content to display.
     * @param modifier Modifier applied to the text layout.
     * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
     * @param textAlign Optional horizontal paragraph alignment.
     * @param fontWeight Optional weight override.
     * @param overflow Overflow handling mode when text exceeds available bounds.
     * @param maxLines Maximum number of lines permitted before applying [overflow].
     */
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
        text = text,
        style = MeteoMartoTheme.typography.headlineMedium,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
    )

    /**
     * Renders [text] using the `headlineSmall` typography token.
     *
     * @param text String content to display.
     * @param modifier Modifier applied to the text layout.
     * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
     * @param textAlign Optional horizontal paragraph alignment.
     * @param fontWeight Optional weight override.
     * @param overflow Overflow handling mode when text exceeds available bounds.
     * @param maxLines Maximum number of lines permitted before applying [overflow].
     */
    @Composable
    fun HeadlineSmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontWeight: FontWeight? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE,
    ) = MmText(
        text = text,
        style = MeteoMartoTheme.typography.headlineSmall,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
    )

    /**
     * Renders [text] using the `titleLarge` typography token.
     *
     * @param text String content to display.
     * @param modifier Modifier applied to the text layout.
     * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
     * @param textAlign Optional horizontal paragraph alignment.
     * @param fontWeight Optional weight override.
     * @param overflow Overflow handling mode when text exceeds available bounds.
     * @param maxLines Maximum number of lines permitted before applying [overflow].
     */
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
        text = text,
        style = MeteoMartoTheme.typography.titleLarge,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
    )

    /**
     * Renders [text] using the `titleMedium` typography token.
     *
     * @param text String content to display.
     * @param modifier Modifier applied to the text layout.
     * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
     * @param textAlign Optional horizontal paragraph alignment.
     * @param fontWeight Optional weight override.
     * @param overflow Overflow handling mode when text exceeds available bounds.
     * @param maxLines Maximum number of lines permitted before applying [overflow].
     */
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
        text = text,
        style = MeteoMartoTheme.typography.titleMedium,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
    )

    /**
     * Renders [text] using the `titleSmall` typography token.
     *
     * @param text String content to display.
     * @param modifier Modifier applied to the text layout.
     * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
     * @param textAlign Optional horizontal paragraph alignment.
     * @param fontWeight Optional weight override.
     * @param overflow Overflow handling mode when text exceeds available bounds.
     * @param maxLines Maximum number of lines permitted before applying [overflow].
     */
    @Composable
    fun TitleSmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontWeight: FontWeight? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE,
    ) = MmText(
        text = text,
        style = MeteoMartoTheme.typography.titleSmall,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
    )

    /**
     * Renders [text] using the `bodyLarge` typography token.
     *
     * @param text String content to display.
     * @param modifier Modifier applied to the text layout.
     * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
     * @param textAlign Optional horizontal paragraph alignment.
     * @param fontWeight Optional weight override.
     * @param overflow Overflow handling mode when text exceeds available bounds.
     * @param maxLines Maximum number of lines permitted before applying [overflow].
     */
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
        text = text,
        style = MeteoMartoTheme.typography.bodyLarge,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
    )

    /**
     * Renders [text] using the `bodyMedium` typography token.
     *
     * @param text String content to display.
     * @param modifier Modifier applied to the text layout.
     * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
     * @param textAlign Optional horizontal paragraph alignment.
     * @param fontWeight Optional weight override.
     * @param overflow Overflow handling mode when text exceeds available bounds.
     * @param maxLines Maximum number of lines permitted before applying [overflow].
     */
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
        text = text,
        style = MeteoMartoTheme.typography.bodyMedium,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
    )

    /**
     * Renders [text] using the `bodySmall` typography token.
     *
     * @param text String content to display.
     * @param modifier Modifier applied to the text layout.
     * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
     * @param textAlign Optional horizontal paragraph alignment.
     * @param fontWeight Optional weight override.
     * @param overflow Overflow handling mode when text exceeds available bounds.
     * @param maxLines Maximum number of lines permitted before applying [overflow].
     */
    @Composable
    fun BodySmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontWeight: FontWeight? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE,
    ) = MmText(
        text = text,
        style = MeteoMartoTheme.typography.bodySmall,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
    )

    /**
     * Renders [text] using the `labelLarge` typography token.
     *
     * @param text String content to display.
     * @param modifier Modifier applied to the text layout.
     * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
     * @param textAlign Optional horizontal paragraph alignment.
     * @param fontWeight Optional weight override.
     * @param overflow Overflow handling mode when text exceeds available bounds.
     * @param maxLines Maximum number of lines permitted before applying [overflow].
     */
    @Composable
    fun LabelLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontWeight: FontWeight? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE,
    ) = MmText(
        text = text,
        style = MeteoMartoTheme.typography.labelLarge,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
    )

    /**
     * Renders [text] using the `labelMedium` typography token.
     *
     * @param text String content to display.
     * @param modifier Modifier applied to the text layout.
     * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
     * @param textAlign Optional horizontal paragraph alignment.
     * @param fontWeight Optional weight override.
     * @param overflow Overflow handling mode when text exceeds available bounds.
     * @param maxLines Maximum number of lines permitted before applying [overflow].
     */
    @Composable
    fun LabelMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontWeight: FontWeight? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        maxLines: Int = Int.MAX_VALUE,
    ) = MmText(
        text = text,
        style = MeteoMartoTheme.typography.labelMedium,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
    )

    /**
     * Renders [text] using the `labelSmall` typography token.
     *
     * @param text String content to display.
     * @param modifier Modifier applied to the text layout.
     * @param color Foreground color override, or `Color.Unspecified` to inherit `LocalContentColor`.
     * @param textAlign Optional horizontal paragraph alignment.
     * @param fontWeight Optional weight override.
     * @param overflow Overflow handling mode when text exceeds available bounds.
     * @param maxLines Maximum number of lines permitted before applying [overflow].
     */
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
        text = text,
        style = MeteoMartoTheme.typography.labelSmall,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
    )
}

@MmPreview
@Composable
private fun MmTextPreview() {
    MeteoMartoTheme {
        Surface(color = MeteoMartoTheme.colors.background) {
            Column(
                modifier = Modifier.padding(MeteoMartoTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MeteoMartoTheme.spacing.extraSmall),
            ) {
                MmText.DisplayLarge("Display Large")
                MmText.HeadlineMedium("Headline Medium")
                MmText.TitleMedium("Title Medium")
                MmText.BodyLarge("Body Large")
                MmText.BodyMedium("Body Medium")
                MmText.BodySmall("Body Small")
                MmText.LabelLarge("Label Large")
                MmText.LabelMedium("Label Medium")
                MmText.LabelSmall("Label Small")
            }
        }
    }
}

@MmDynamicPalettesPreview
@Composable
private fun MmTextPalettesPreview() {
    MmDynamicPalettesShowcase {
        MmCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MeteoMartoTheme.spacing.medium),
        ) {
            MmText.TitleLarge(
                text = "Sabadell · 28°C",
                color = MeteoMartoTheme.colors.onSurface,
            )
            MmText.BodyMedium(
                text = "Supporting metadata",
                color = MeteoMartoTheme.colors.onSurfaceVariant,
            )
            MmText.LabelLarge(
                text = "Max 32°C",
                color = MeteoMartoTheme.colors.hot,
            )
            MmText.LabelLarge(
                text = "Min 18°C",
                color = MeteoMartoTheme.colors.cold,
            )
        }
    }
}
