package com.martorell.albert.meteomartocompose.ui.theme

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

/**
 * RobotoFlex defines the variable font family for the MeteoMarto Design System.
 * It uses the standard OpenType axes:
 * - 'wght': Weight (400=Regular, 500=Medium)
 * - 'wdth': Width (100=Normal)
 * - 'slnt': Slant (0=Upright)
 * - 'opsz': Optical Size (14=Standard reading optimization)
 *
 * @see [MM-02 Technical Record](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/docs/features/design_system/MM-02-Design-System.md) for detailed axes rationale.
 */
@OptIn(ExperimentalTextApi::class)
internal val RobotoFlex = FontFamily(
    Font(
        resId = R.font.roboto_flex,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(MMFontWeight.NORMAL),
            FontVariation.width(MMFontAxes.WIDTH_DEFAULT),
            FontVariation.slant(MMFontAxes.SLANT_DEFAULT),
            FontVariation.Setting(MMFontAxes.OPSZ_TAG, MMFontAxes.OPSZ_DEFAULT)
        )
    )
)

/**
 * MMTypography defines the text styles for the MeteoMarto Design System.
 * Values are based on the Material 3 Type Scale and use the Roboto Flex variable font.
 *
 * @see [MM-02 Technical Record](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/docs/features/design_system/MM-02-Design-System.md) for the reference table.
 */
@Immutable
data class MMTypography(
    val displayLarge: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    val displayMedium: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    val displaySmall: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    val headlineLarge: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    val headlineMedium: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    val headlineSmall: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    val titleLarge: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    val titleMedium: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    val titleSmall: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    val bodyLarge: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    val bodyMedium: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    val bodySmall: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    val labelLarge: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    val labelMedium: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    val labelSmall: TextStyle = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

/**
 * Constants for variable font axes values to avoid magic numbers.
 */
object MMFontAxes {
    const val WIDTH_DEFAULT = 100f
    const val SLANT_DEFAULT = 0f
    const val OPSZ_DEFAULT = 14f
    const val OPSZ_TAG = "opsz"
}

/**
 * Common font weight constants for the Design System.
 * These values follow the universal OpenType standard (ISO/IEC 14496-22), 
 * ensuring consistency across platforms and design tools (e.g., Figma).
 * @see [OpenType OS/2 Weight Class](https://learn.microsoft.com/en-us/typography/opentype/spec/os2#usweightclass)
 */
object MMFontWeight {
    const val NORMAL = 400
    const val MEDIUM = 500
    const val SEMIBOLD = 600
    const val BOLD = 700
}

val LocalMMTypography = staticCompositionLocalOf { MMTypography() }