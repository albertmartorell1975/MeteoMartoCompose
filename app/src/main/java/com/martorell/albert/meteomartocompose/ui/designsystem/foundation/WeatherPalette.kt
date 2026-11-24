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
    background = Color(0xFFF8F9FA),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1C1E),
    primary = Color(0xFF4A627A),
)

/**
 * Warm high-luminance palette applied during sunny daytime conditions.
 */
val SunnyPalette = SimpleWeatherPalette(
    background = Color(0xFFFFF8F0),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF2D2314),
    primary = Color(0xFFE68A00),
)

/**
 * Cool atmospheric palette applied during rainy daytime conditions.
 */
val RainPalette = SimpleWeatherPalette(
    background = Color(0xFFEBF2F7),
    surface = Color(0xFFF4F8FB),
    onSurface = Color(0xFF13202C),
    primary = Color(0xFF2C5282),
)

/**
 * Low-luminance dark palette applied during nighttime or dark mode.
 */
val NightPalette = SimpleWeatherPalette(
    background = Color(0xFF0E1419),
    surface = Color(0xFF161C21),
    onSurface = Color(0xFFE1E2E5),
    primary = Color(0xFF9D8DF1),
)

/**
 * High-urgency alert palette applied when extreme weather warnings are active.
 */
val ExtremeAlertPalette = SimpleWeatherPalette(
    background = Color(0xFF701A1A),
    surface = Color(0xFF8B2323),
    onSurface = Color(0xFFFFFFFF),
    primary = Color(0xFFB5C4FF),
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
