package com.martorell.albert.meteomartocompose.ui.designsystem.foundation

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.martorell.albert.meteomartocompose.R

@OptIn(ExperimentalTextApi::class)
private fun robotoFlexFont(
    weight: FontWeight,
    weightAxis: Int,
) = Font(
    resId = R.font.roboto_flex,
    weight = weight,
    variationSettings = FontVariation.Settings(
        FontVariation.weight(weightAxis),
        FontVariation.width(FndFontAxes.WIDTH_DEFAULT),
        FontVariation.slant(FndFontAxes.SLANT_DEFAULT),
        FontVariation.Setting(FndFontAxes.OPSZ_TAG, FndFontAxes.OPSZ_DEFAULT),
    ),
)

/**
 * Variable Roboto Flex font family registering discrete weight axis configurations.
 */
internal val RobotoFlex = FontFamily(
    robotoFlexFont(weight = FontWeight.Normal, weightAxis = FndFontWeight.NORMAL),
    robotoFlexFont(weight = FontWeight.Medium, weightAxis = FndFontWeight.MEDIUM),
    robotoFlexFont(weight = FontWeight.SemiBold, weightAxis = FndFontWeight.SEMIBOLD),
    robotoFlexFont(weight = FontWeight.Bold, weightAxis = FndFontWeight.BOLD),
)

/**
 * Typography scale for the MeteoMarto design system backed by variable Roboto Flex weights.
 *
 * Every text style enforces a minimum size of 12.sp to preserve legibility across all screen scales.
 *
 * @property displayLarge Largest hero display text style (57.sp).
 * @property displayMedium Medium hero display text style (45.sp).
 * @property displaySmall Compact hero display text style (36.sp).
 * @property headlineLarge Large section headline text style (32.sp).
 * @property headlineMedium Medium section headline text style (28.sp).
 * @property headlineSmall Small section headline text style (24.sp).
 * @property titleLarge Prominent screen and dialog title text style (22.sp).
 * @property titleMedium Medium component and card title text style (16.sp).
 * @property titleSmall Compact subheader and list title text style (14.sp).
 * @property bodyLarge Primary long-form body copy text style (16.sp).
 * @property bodyMedium Standard body copy text style (14.sp).
 * @property bodySmall Supporting caption and metadata body text style (12.sp).
 * @property labelLarge Primary button and interactive control label text style (14.sp).
 * @property labelMedium Secondary chip and tab label text style (12.sp).
 * @property labelSmall Smallest supporting label text style, capped at a 12.sp minimum.
 */
@Immutable
data class FndTypography(
    val displayLarge: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    val displayMedium: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    val displaySmall: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    val headlineLarge: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    val headlineMedium: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    val headlineSmall: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    val titleLarge: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    val titleMedium: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    val titleSmall: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    val bodyLarge: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    val bodyMedium: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    val bodySmall: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    val labelLarge: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    val labelMedium: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    val labelSmall: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)

/**
 * Converts [FndTypography] foundation tokens into a Material 3 [Typography] configuration.
 *
 * @return Material 3 [Typography] mapping all 15 text scale roles to Roboto Flex styles.
 */
fun FndTypography.toMaterialTypography(): Typography = Typography(
    displayLarge = displayLarge,
    displayMedium = displayMedium,
    displaySmall = displaySmall,
    headlineLarge = headlineLarge,
    headlineMedium = headlineMedium,
    headlineSmall = headlineSmall,
    titleLarge = titleLarge,
    titleMedium = titleMedium,
    titleSmall = titleSmall,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = labelLarge,
    labelMedium = labelMedium,
    labelSmall = labelSmall,
)

/**
 * Default OpenType variable-font axis parameters for Roboto Flex.
 */
object FndFontAxes {
    /** Default horizontal glyph width axis percentage. */
    const val WIDTH_DEFAULT = 100f

    /** Default upright glyph slant angle in degrees. */
    const val SLANT_DEFAULT = 0f

    /** Default optical size axis value in points. */
    const val OPSZ_DEFAULT = 14f

    /** OpenType axis tag for optical size (`opsz`). */
    const val OPSZ_TAG = "opsz"
}

/**
 * Supported Roboto Flex weight axis values aligned with Compose [FontWeight] tiers.
 */
object FndFontWeight {
    /** Regular weight axis value (400). */
    const val NORMAL = 400

    /** Medium weight axis value (500). */
    const val MEDIUM = 500

    /** SemiBold weight axis value (600). */
    const val SEMIBOLD = 600

    /** Bold weight axis value (700). */
    const val BOLD = 700
}

/**
 * CompositionLocal providing the active [FndTypography] scale tokens.
 */
val LocalFndTypography = staticCompositionLocalOf { FndTypography() }
