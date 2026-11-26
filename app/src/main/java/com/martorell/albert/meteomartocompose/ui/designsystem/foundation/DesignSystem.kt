package com.martorell.albert.meteomartocompose.ui.designsystem.foundation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.martorell.albert.meteomartocompose.data.preferences.UserPreferences
import com.martorell.albert.meteomartocompose.domain.cityweather.WeatherCondition

private val DefaultFndSpacing = FndSpacing()
private val DefaultFndDimensions = FndDimensions()
private val DefaultFndTypography = FndTypography()
private val DefaultFndShapes = FndShapes()
private val DefaultMaterialTypography = DefaultFndTypography.toMaterialTypography()
private val DefaultMaterialShapes = DefaultFndShapes.toMaterialShapes()

/**
 * Entry point for accessing the active MeteoMarto design system foundation tokens
 * within a `@Composable` hierarchy.
 */
object MeteoMartoTheme {
    /**
     * Active four-color [SimpleWeatherPalette] for the current theme scope.
     */
    val palette: SimpleWeatherPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalWeatherPalette.current

    /**
     * Active [FndSpacing] grid tokens for the current theme scope.
     */
    val spacing: FndSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalFndSpacing.current

    /**
     * Active [FndDimensions] layout constraint tokens for the current theme scope.
     */
    val dimensions: FndDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalFndDimensions.current

    /**
     * Active semantic [FndColors] tokens derived from the resolved weather [palette].
     */
    val colors: FndColors
        @Composable
        @ReadOnlyComposable
        get() = LocalFndColors.current

    /**
     * Active [FndTypography] scale tokens backed by Roboto Flex.
     */
    val typography: FndTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalFndTypography.current

    /**
     * Active [FndShapes] corner radius tokens for inputs, cards, dialogs, and pills.
     */
    val shapes: FndShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalFndShapes.current
}

/**
 * Applies the deterministic MeteoMarto weather theme, providing foundation tokens and
 * configuring Material 3 without OS wallpaper dynamic-color overrides.
 *
 * @param weatherCondition Active weather condition used when resolving the default [palette].
 * @param isNight Whether nighttime or system dark appearance should be applied.
 * @param isExtremeAlert Whether an extreme weather warning overrides regular condition palettes.
 * @param palette Explicit or resolved [SimpleWeatherPalette] driving the semantic color tokens.
 * @param fontScale User-configured font scaling multiplier applied via [FndDensityProvider].
 * @param content Composable UI tree rendered within the configured theme scope.
 */
@Composable
fun MeteoMartoTheme(
    weatherCondition: WeatherCondition = WeatherCondition.BASELINE,
    isNight: Boolean = isSystemInDarkTheme(),
    isExtremeAlert: Boolean = false,
    palette: SimpleWeatherPalette = resolveWeatherPalette(
        condition = weatherCondition,
        isNight = isNight,
        isExtremeAlert = isExtremeAlert,
    ),
    fontScale: Float = UserPreferences.DEFAULT_FONT_SCALE,
    content: @Composable () -> Unit,
) {
    val colors = FndColors.fromPalette(palette)
    val colorScheme = colors.toMaterialColorScheme()

    FndDensityProvider(userScale = fontScale) {
        CompositionLocalProvider(
            LocalWeatherPalette provides palette,
            LocalFndSpacing provides DefaultFndSpacing,
            LocalFndColors provides colors,
            LocalFndTypography provides DefaultFndTypography,
            LocalFndDimensions provides DefaultFndDimensions,
            LocalFndShapes provides DefaultFndShapes,
        ) {
            MaterialTheme(
                colorScheme = colorScheme,
                typography = DefaultMaterialTypography,
                shapes = DefaultMaterialShapes,
                content = content,
            )
        }
    }
}

/**
 * Maps [FndColors] foundation tokens into a Material 3 light or dark `ColorScheme`
 * based on [FndColors.isLight], ensuring container, surface-container, and outline
 * roles match the active weather palette without Material default fallbacks.
 *
 * @return Configured Material 3 `ColorScheme` instance.
 */
internal fun FndColors.toMaterialColorScheme(): ColorScheme {
    val surfaceContainerLowest = if (isLight) surface else background
    return if (isLight) {
        lightColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSecondaryContainer,
            background = background,
            onBackground = onSurface,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceContainer,
            onSurfaceVariant = onSurfaceVariant,
            surfaceContainerLowest = surfaceContainerLowest,
            surfaceContainerLow = surfaceContainerLow,
            surfaceContainer = surfaceContainer,
            surfaceContainerHigh = surfaceContainerHigh,
            surfaceContainerHighest = surfaceContainerHigh,
            outline = outline,
            outlineVariant = outlineVariant,
            error = error,
            onError = onError,
            errorContainer = errorContainer,
            onErrorContainer = onErrorContainer,
        )
    } else {
        darkColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSecondaryContainer,
            background = background,
            onBackground = onSurface,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceContainer,
            onSurfaceVariant = onSurfaceVariant,
            surfaceContainerLowest = surfaceContainerLowest,
            surfaceContainerLow = surfaceContainerLow,
            surfaceContainer = surfaceContainer,
            surfaceContainerHigh = surfaceContainerHigh,
            surfaceContainerHighest = surfaceContainerHigh,
            outline = outline,
            outlineVariant = outlineVariant,
            error = error,
            onError = onError,
            errorContainer = errorContainer,
            onErrorContainer = onErrorContainer,
        )
    }
}
