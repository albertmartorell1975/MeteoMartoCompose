package com.martorell.albert.meteomartocompose.ui.designsystem.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.martorell.albert.meteomartocompose.domain.cityweather.WeatherCondition

/**
 * Core four-color weather palette representing a single climate theme state.
 *
 * @property background Root screen backdrop color.
 * @property surface Elevated container and card surface color.
 * @property onSurface Primary text and icon color rendered over [background] and [surface].
 * @property primary Primary accent color for interactive actions and key highlights.
 */
@Immutable
data class SimpleWeatherPalette(
    val background: Color,
    val surface: Color,
    val onSurface: Color,
    val primary: Color,
)

/**
 * Neutral daytime baseline palette used for static screens and default conditions.
 */
val DayBaselinePalette = SimpleWeatherPalette(
    background = FndColorPrimitives.DayBaselineBackground,
    surface = Color.White,
    onSurface = FndColorPrimitives.DayBaselineOnSurface,
    primary = FndColorPrimitives.DayBaselinePrimary,
)

/**
 * Warm high-luminance palette applied during sunny daytime conditions.
 */
val SunnyPalette = SimpleWeatherPalette(
    background = FndColorPrimitives.SunnyBackground,
    surface = Color.White,
    onSurface = FndColorPrimitives.SunnyOnSurface,
    primary = FndColorPrimitives.SunnyPrimary,
)

/**
 * Cool atmospheric palette applied during rainy daytime conditions.
 */
val RainPalette = SimpleWeatherPalette(
    background = FndColorPrimitives.RainBackground,
    surface = FndColorPrimitives.RainSurface,
    onSurface = FndColorPrimitives.RainOnSurface,
    primary = FndColorPrimitives.RainPrimary,
)

/**
 * Low-luminance dark palette applied during nighttime or dark mode.
 */
val NightPalette = SimpleWeatherPalette(
    background = FndColorPrimitives.NightBackground,
    surface = FndColorPrimitives.NightSurface,
    onSurface = FndColorPrimitives.NightOnSurface,
    primary = FndColorPrimitives.NightPrimary,
)

/**
 * High-urgency alert palette applied when extreme weather warnings are active.
 */
val ExtremeAlertPalette = SimpleWeatherPalette(
    background = FndColorPrimitives.ExtremeAlertBackground,
    surface = FndColorPrimitives.ExtremeAlertSurface,
    onSurface = Color.White,
    primary = FndColorPrimitives.ExtremeAlertPrimary,
)

/**
 * CompositionLocal providing the active [SimpleWeatherPalette], defaulting to [DayBaselinePalette].
 */
val LocalWeatherPalette = compositionLocalOf { DayBaselinePalette }

/**
 * Resolves the deterministic [SimpleWeatherPalette] using strict precedence:
 * `ExtremeAlert > Night > Sunny > Rain > DayBaseline`.
 *
 * @param condition Current weather condition classification.
 * @param isNight Whether nighttime or dark appearance is active.
 * @param isExtremeAlert Whether an extreme weather alert overrides regular conditions.
 * @return The resolved [SimpleWeatherPalette] matching the highest-precedence condition.
 */
fun resolveWeatherPalette(
    condition: WeatherCondition = WeatherCondition.BASELINE,
    isNight: Boolean = false,
    isExtremeAlert: Boolean = false,
): SimpleWeatherPalette {
    if (isExtremeAlert) return ExtremeAlertPalette
    if (isNight) return NightPalette
    return when (condition) {
        WeatherCondition.SUNNY -> SunnyPalette
        WeatherCondition.RAIN -> RainPalette
        WeatherCondition.BASELINE -> DayBaselinePalette
    }
}
