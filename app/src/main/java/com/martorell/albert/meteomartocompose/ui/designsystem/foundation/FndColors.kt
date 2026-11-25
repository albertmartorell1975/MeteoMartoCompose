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
     * Factory for creating [FndColors] from a weather palette.
     */
    companion object {
        private const val LIGHT_LUMINANCE_THRESHOLD = 0.5f

        /**
         * Returns the semantic colors for [palette], using a preset or deriving defaults for custom palettes.
         *
         * @param palette Weather palette to convert.
         * @return Matching [FndColors] token set.
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
 * Semantic colors for [DayBaselinePalette].
 */
val DayBaselineFndColors = FndColors(
    background = DayBaselinePalette.background,
    primary = DayBaselinePalette.primary,
    onPrimary = Color.White,
    secondary = FndColorPrimitives.DayBaselineSecondary,
    onSecondary = Color.White,
    surface = DayBaselinePalette.surface,
    onSurface = DayBaselinePalette.onSurface,
    error = FndColorPrimitives.ErrorLight,
    onError = Color.White,
    errorContainer = FndColorPrimitives.ErrorContainerLight,
    onErrorContainer = FndColorPrimitives.OnErrorContainerLight,
    success = FndColorPrimitives.SuccessLight,
    onSuccess = Color.White,
    hot = FndColorPrimitives.ThermalHotLight,
    cold = FndColorPrimitives.ThermalColdLight,
    isLight = true,
)

/**
 * Semantic colors for [SunnyPalette].
 */
val SunnyFndColors = FndColors(
    background = SunnyPalette.background,
    primary = SunnyPalette.primary,
    onPrimary = FndColorPrimitives.SunnyOnSurface,
    secondary = FndColorPrimitives.SunnySecondary,
    onSecondary = Color.White,
    surface = SunnyPalette.surface,
    onSurface = SunnyPalette.onSurface,
    error = FndColorPrimitives.ErrorLight,
    onError = Color.White,
    errorContainer = FndColorPrimitives.ErrorContainerLight,
    onErrorContainer = FndColorPrimitives.OnErrorContainerLight,
    success = FndColorPrimitives.SuccessLight,
    onSuccess = Color.White,
    hot = FndColorPrimitives.ThermalHotLight,
    cold = FndColorPrimitives.ThermalColdLight,
    isLight = true,
)

/**
 * Semantic colors for [RainPalette].
 */
val RainFndColors = FndColors(
    background = RainPalette.background,
    primary = RainPalette.primary,
    onPrimary = Color.White,
    secondary = FndColorPrimitives.RainSecondary,
    onSecondary = Color.White,
    surface = RainPalette.surface,
    onSurface = RainPalette.onSurface,
    error = FndColorPrimitives.ErrorLight,
    onError = Color.White,
    errorContainer = FndColorPrimitives.ErrorContainerLight,
    onErrorContainer = FndColorPrimitives.OnErrorContainerLight,
    success = FndColorPrimitives.SuccessLight,
    onSuccess = Color.White,
    hot = FndColorPrimitives.ThermalHotLight,
    cold = FndColorPrimitives.ThermalColdRain,
    isLight = true,
)

/**
 * Semantic colors for [NightPalette].
 */
val NightFndColors = FndColors(
    background = NightPalette.background,
    primary = NightPalette.primary,
    onPrimary = FndColorPrimitives.NightBackground,
    secondary = FndColorPrimitives.NightSecondary,
    onSecondary = FndColorPrimitives.NightBackground,
    surface = NightPalette.surface,
    onSurface = NightPalette.onSurface,
    error = FndColorPrimitives.ErrorDark,
    onError = FndColorPrimitives.OnErrorDark,
    errorContainer = FndColorPrimitives.ErrorContainerDark,
    onErrorContainer = FndColorPrimitives.ErrorContainerLight,
    success = FndColorPrimitives.SuccessDark,
    onSuccess = FndColorPrimitives.OnSuccessDark,
    hot = FndColorPrimitives.ThermalHotNight,
    cold = FndColorPrimitives.ThermalColdNight,
    isLight = false,
)

/**
 * Semantic colors for [ExtremeAlertPalette].
 */
val ExtremeAlertFndColors = FndColors(
    background = ExtremeAlertPalette.background,
    primary = ExtremeAlertPalette.primary,
    onPrimary = FndColorPrimitives.DayBaselineOnSurface,
    secondary = FndColorPrimitives.ErrorContainerLight,
    onSecondary = FndColorPrimitives.OnErrorContainerLight,
    surface = ExtremeAlertPalette.surface,
    onSurface = ExtremeAlertPalette.onSurface,
    error = FndColorPrimitives.ErrorContainerLight,
    onError = FndColorPrimitives.OnErrorContainerLight,
    errorContainer = FndColorPrimitives.ErrorContainerAlert,
    onErrorContainer = Color.White,
    success = FndColorPrimitives.SuccessAlert,
    onSuccess = FndColorPrimitives.OnSuccessDark,
    hot = FndColorPrimitives.ErrorDark,
    cold = FndColorPrimitives.ExtremeAlertPrimary,
    isLight = false,
)

/**
 * CompositionLocal providing the active [FndColors], defaulting to [DayBaselineFndColors].
 */
val LocalFndColors = compositionLocalOf { DayBaselineFndColors }
