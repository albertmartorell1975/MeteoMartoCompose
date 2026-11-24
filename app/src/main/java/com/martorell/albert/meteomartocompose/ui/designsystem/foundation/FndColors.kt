package com.martorell.albert.meteomartocompose.ui.designsystem.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/**
 * Semantic foundation color tokens for the MeteoMarto design system.
 *
 * All roles are calibrated to maintain WCAG AA contrast against their paired containers.
 *
 * @property background Root backdrop color behind primary screen layouts.
 * @property primary Primary interactive brand and action color.
 * @property onPrimary Foreground content color rendered on top of [primary].
 * @property secondary Secondary supporting accent color.
 * @property onSecondary Foreground content color rendered on top of [secondary].
 * @property surface Elevated card, sheet, and component surface color.
 * @property onSurface Primary text and iconography color rendered on [surface] and [background].
 * @property error Error state highlight color.
 * @property onError Foreground content color rendered on top of [error].
 * @property errorContainer Subtle container color for error banners and states.
 * @property onErrorContainer Foreground content color rendered inside [errorContainer].
 * @property success Positive confirmation color.
 * @property onSuccess Foreground content color rendered on top of [success].
 * @property hot Semantic thermal accent for high-temperature indicators.
 * @property cold Semantic thermal accent for low-temperature indicators.
 * @property isLight Whether this color set represents a light-mode surface polarity.
 */
@Immutable
data class FndColors(
    val background: Color,
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val surface: Color,
    val onSurface: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val success: Color,
    val onSuccess: Color,
    val hot: Color,
    val cold: Color,
    val isLight: Boolean,
) {
    /**
     * Factory methods for deriving semantic [FndColors] tokens from weather palettes.
     */
    companion object {
        private const val LIGHT_LUMINANCE_THRESHOLD = 0.5f

        /**
         * Maps a [SimpleWeatherPalette] into a WCAG-compliant [FndColors] token set,
         * returning pre-calibrated presets for canonical palettes or deriving accessible
         * defaults for custom palettes.
         *
         * @param palette Source weather palette to expand into full semantic color roles.
         * @return Resolved [FndColors] matching the palette polarity and contrast constraints.
         */
        fun fromPalette(palette: SimpleWeatherPalette): FndColors = when (palette) {
            DayBaselinePalette -> DayBaselineFndColors
            SunnyPalette -> SunnyFndColors
            RainPalette -> RainFndColors
            NightPalette -> NightFndColors
            ExtremeAlertPalette -> ExtremeAlertFndColors
            else -> buildCustomFndColors(palette)
        }

        private fun buildCustomFndColors(palette: SimpleWeatherPalette): FndColors {
            val isLightBackground = palette.background.luminance() >= LIGHT_LUMINANCE_THRESHOLD
            val baseColors = if (isLightBackground) DayBaselineFndColors else NightFndColors
            val resolvedOnPrimary = if (palette.primary.luminance() >= LIGHT_LUMINANCE_THRESHOLD) {
                DayBaselinePalette.onSurface
            } else {
                Color.White
            }
            return baseColors.copy(
                background = palette.background,
                primary = palette.primary,
                onPrimary = resolvedOnPrimary,
                surface = palette.surface,
                onSurface = palette.onSurface,
                isLight = isLightBackground,
            )
        }
    }
}

/**
 * Pre-calibrated semantic color tokens derived from [DayBaselinePalette].
 */
val DayBaselineFndColors = FndColors(
    background = DayBaselinePalette.background,
    primary = DayBaselinePalette.primary,
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF53606E),
    onSecondary = Color(0xFFFFFFFF),
    surface = DayBaselinePalette.surface,
    onSurface = DayBaselinePalette.onSurface,
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    success = Color(0xFF2E7D32),
    onSuccess = Color(0xFFFFFFFF),
    hot = Color(0xFFD32F2F),
    cold = Color(0xFF1976D2),
    isLight = true,
)

/**
 * Pre-calibrated semantic color tokens derived from [SunnyPalette].
 */
val SunnyFndColors = FndColors(
    background = SunnyPalette.background,
    primary = SunnyPalette.primary,
    onPrimary = Color(0xFF2D2314),
    secondary = Color(0xFF6E5D40),
    onSecondary = Color(0xFFFFFFFF),
    surface = SunnyPalette.surface,
    onSurface = SunnyPalette.onSurface,
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    success = Color(0xFF2E7D32),
    onSuccess = Color(0xFFFFFFFF),
    hot = Color(0xFFD32F2F),
    cold = Color(0xFF1976D2),
    isLight = true,
)

/**
 * Pre-calibrated semantic color tokens derived from [RainPalette].
 */
val RainFndColors = FndColors(
    background = RainPalette.background,
    primary = RainPalette.primary,
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF47607A),
    onSecondary = Color(0xFFFFFFFF),
    surface = RainPalette.surface,
    onSurface = RainPalette.onSurface,
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    success = Color(0xFF2E7D32),
    onSuccess = Color(0xFFFFFFFF),
    hot = Color(0xFFD32F2F),
    cold = Color(0xFF1565C0),
    isLight = true,
)

/**
 * Pre-calibrated semantic color tokens derived from [NightPalette].
 */
val NightFndColors = FndColors(
    background = NightPalette.background,
    primary = NightPalette.primary,
    onPrimary = Color(0xFF0E1419),
    secondary = Color(0xFFB8C4D0),
    onSecondary = Color(0xFF0E1419),
    surface = NightPalette.surface,
    onSurface = NightPalette.onSurface,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    success = Color(0xFF81C784),
    onSuccess = Color(0xFF003300),
    hot = Color(0xFFFF8A80),
    cold = Color(0xFF82B1FF),
    isLight = false,
)

/**
 * Pre-calibrated semantic color tokens derived from [ExtremeAlertPalette].
 */
val ExtremeAlertFndColors = FndColors(
    background = ExtremeAlertPalette.background,
    primary = ExtremeAlertPalette.primary,
    onPrimary = Color(0xFF1A1C1E),
    secondary = Color(0xFFFFDAD6),
    onSecondary = Color(0xFF410002),
    surface = ExtremeAlertPalette.surface,
    onSurface = ExtremeAlertPalette.onSurface,
    error = Color(0xFFFFDAD6),
    onError = Color(0xFF410002),
    errorContainer = Color(0xFF530F0F),
    onErrorContainer = Color(0xFFFFFFFF),
    success = Color(0xFFA5D6A7),
    onSuccess = Color(0xFF003300),
    hot = Color(0xFFFFB4AB),
    cold = Color(0xFFB5C4FF),
    isLight = false,
)

/**
 * CompositionLocal providing the active [FndColors], defaulting to [DayBaselineFndColors].
 */
val LocalFndColors = compositionLocalOf { DayBaselineFndColors }
