package com.martorell.albert.meteomartocompose.ui.designsystem.foundation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.martorell.albert.meteomartocompose.domain.cityweather.WeatherCondition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class WeatherThemePaletteTest {

    @Test
    fun `resolveWeatherPalette enforces strict precedence and WCAG AA contrast across all five palettes`() {
        for (condition in WeatherCondition.entries) {
            for (isNight in listOf(false, true)) {
                assertEquals(
                    "ExtremeAlert must outrank isNight=$isNight and condition=$condition",
                    ExtremeAlertPalette,
                    resolveWeatherPalette(
                        condition = condition,
                        isNight = isNight,
                        isExtremeAlert = true,
                    ),
                )
            }
            assertEquals(
                "Night must outrank condition=$condition when alert is false",
                NightPalette,
                resolveWeatherPalette(
                    condition = condition,
                    isNight = true,
                    isExtremeAlert = false,
                ),
            )
        }

        assertEquals(
            SunnyPalette,
            resolveWeatherPalette(condition = WeatherCondition.SUNNY, isNight = false, isExtremeAlert = false),
        )
        assertEquals(
            RainPalette,
            resolveWeatherPalette(condition = WeatherCondition.RAIN, isNight = false, isExtremeAlert = false),
        )
        assertEquals(
            DayBaselinePalette,
            resolveWeatherPalette(condition = WeatherCondition.BASELINE, isNight = false, isExtremeAlert = false),
        )

        val expectedPalettes = listOf(
            PaletteSpec(
                name = "DayBaseline",
                palette = DayBaselinePalette,
                background = Color(0xFFF8F9FA),
                surface = Color(0xFFFFFFFF),
                onSurface = Color(0xFF1A1C1E),
                primary = Color(0xFF4A627A),
                isLight = true,
            ),
            PaletteSpec(
                name = "Sunny",
                palette = SunnyPalette,
                background = Color(0xFFFFF8F0),
                surface = Color(0xFFFFFFFF),
                onSurface = Color(0xFF2D2314),
                primary = Color(0xFFE68A00),
                isLight = true,
            ),
            PaletteSpec(
                name = "Rain",
                palette = RainPalette,
                background = Color(0xFFEBF2F7),
                surface = Color(0xFFF4F8FB),
                onSurface = Color(0xFF13202C),
                primary = Color(0xFF2C5282),
                isLight = true,
            ),
            PaletteSpec(
                name = "Night",
                palette = NightPalette,
                background = Color(0xFF0E1419),
                surface = Color(0xFF161C21),
                onSurface = Color(0xFFE1E2E5),
                primary = Color(0xFF9D8DF1),
                isLight = false,
            ),
            PaletteSpec(
                name = "ExtremeAlert",
                palette = ExtremeAlertPalette,
                background = Color(0xFF701A1A),
                surface = Color(0xFF8B2323),
                onSurface = Color(0xFFFFFFFF),
                primary = Color(0xFFB5C4FF),
                isLight = false,
            ),
        )

        expectedPalettes.forEach { spec ->
            assertEquals("${spec.name}.background", spec.background, spec.palette.background)
            assertEquals("${spec.name}.surface", spec.surface, spec.palette.surface)
            assertEquals("${spec.name}.onSurface", spec.onSurface, spec.palette.onSurface)
            assertEquals("${spec.name}.primary", spec.primary, spec.palette.primary)

            val colors = FndColors.fromPalette(spec.palette)
            assertEquals("${spec.name}.isLight", spec.isLight, colors.isLight)
            assertEquals("${spec.name}.colors.background", spec.background, colors.background)
            assertEquals("${spec.name}.colors.surface", spec.surface, colors.surface)
            assertEquals("${spec.name}.colors.onSurface", spec.onSurface, colors.onSurface)
            assertEquals("${spec.name}.colors.primary", spec.primary, colors.primary)

            assertContrastAtLeast("${spec.name} onSurface/background", colors.onSurface, colors.background)
            assertContrastAtLeast("${spec.name} onSurface/surface", colors.onSurface, colors.surface)
            assertContrastAtLeast("${spec.name} onPrimary/primary", colors.onPrimary, colors.primary)
            assertContrastAtLeast("${spec.name} hot/surface", colors.hot, colors.surface)
            assertContrastAtLeast("${spec.name} cold/surface", colors.cold, colors.surface)
        }

        val customDark = SimpleWeatherPalette(
            background = Color(0xFF101820),
            surface = Color(0xFF1A242F),
            onSurface = Color(0xFFF0F4F8),
            primary = Color(0xFF8AB4F8),
        )
        val customLight = SimpleWeatherPalette(
            background = Color(0xFFF5F7FA),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF111827),
            primary = Color(0xFF1D4ED8),
        )
        assertFalse(FndColors.fromPalette(customDark).isLight)
        assertTrue(FndColors.fromPalette(customLight).isLight)
    }

    @Test
    fun `fndTypography and material typography bridge enforce 12sp minimum across all 15 styles`() {
        val fndTypography = FndTypography()
        val materialTypography = fndTypography.toMaterialTypography()

        val fndStyles = listOf(
            "displayLarge" to fndTypography.displayLarge,
            "displayMedium" to fndTypography.displayMedium,
            "displaySmall" to fndTypography.displaySmall,
            "headlineLarge" to fndTypography.headlineLarge,
            "headlineMedium" to fndTypography.headlineMedium,
            "headlineSmall" to fndTypography.headlineSmall,
            "titleLarge" to fndTypography.titleLarge,
            "titleMedium" to fndTypography.titleMedium,
            "titleSmall" to fndTypography.titleSmall,
            "bodyLarge" to fndTypography.bodyLarge,
            "bodyMedium" to fndTypography.bodyMedium,
            "bodySmall" to fndTypography.bodySmall,
            "labelLarge" to fndTypography.labelLarge,
            "labelMedium" to fndTypography.labelMedium,
            "labelSmall" to fndTypography.labelSmall,
        )

        val materialStyles = listOf(
            "displayLarge" to materialTypography.displayLarge,
            "displayMedium" to materialTypography.displayMedium,
            "displaySmall" to materialTypography.displaySmall,
            "headlineLarge" to materialTypography.headlineLarge,
            "headlineMedium" to materialTypography.headlineMedium,
            "headlineSmall" to materialTypography.headlineSmall,
            "titleLarge" to materialTypography.titleLarge,
            "titleMedium" to materialTypography.titleMedium,
            "titleSmall" to materialTypography.titleSmall,
            "bodyLarge" to materialTypography.bodyLarge,
            "bodyMedium" to materialTypography.bodyMedium,
            "bodySmall" to materialTypography.bodySmall,
            "labelLarge" to materialTypography.labelLarge,
            "labelMedium" to materialTypography.labelMedium,
            "labelSmall" to materialTypography.labelSmall,
        )

        assertEquals(TOTAL_MATERIAL_TEXT_STYLES, fndStyles.size)
        assertEquals(fndStyles, materialStyles)

        fndStyles.forEach { (name, style) ->
            assertEquals("$name must use RobotoFlex", RobotoFlex, style.fontFamily)
            assertTrue(
                "$name fontSize (${style.fontSize}) must be >= 12.sp",
                style.fontSize.value >= MIN_ACCESSIBLE_FONT_SIZE_SP,
            )
            assertTrue(
                "$name lineHeight (${style.lineHeight}) must be >= fontSize (${style.fontSize})",
                style.lineHeight.value >= style.fontSize.value,
            )
        }

        assertEquals(12.sp, fndTypography.labelSmall.fontSize)
        assertEquals(16.sp, fndTypography.labelSmall.lineHeight)
        assertEquals(FontWeight.Medium, fndTypography.labelSmall.fontWeight)
        assertEquals(12.sp, materialTypography.labelSmall.fontSize)
        assertEquals(16.sp, materialTypography.labelSmall.lineHeight)
    }

    @Test
    fun `fndColors container and surface roles satisfy WCAG AA contrast across all five palettes`() {
        val customLight = SimpleWeatherPalette(
            background = Color(0xFFF5F7FA),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF111827),
            primary = Color(0xFF1D4ED8),
        )
        val customDark = SimpleWeatherPalette(
            background = Color(0xFF101820),
            surface = Color(0xFF1A242F),
            onSurface = Color(0xFFF0F4F8),
            primary = Color(0xFF8AB4F8),
        )
        val palettes = listOf(
            "DayBaseline" to DayBaselinePalette,
            "Sunny" to SunnyPalette,
            "Rain" to RainPalette,
            "Night" to NightPalette,
            "ExtremeAlert" to ExtremeAlertPalette,
            "CustomLight" to customLight,
            "CustomDark" to customDark,
        )

        palettes.forEach { (name, palette) ->
            val colors = FndColors.fromPalette(palette)
            val scheme = colors.toMaterialColorScheme()
            val expectedLowest = if (colors.isLight) colors.surface else colors.background

            assertEquals("$name.primaryContainer", colors.primaryContainer, scheme.primaryContainer)
            assertEquals("$name.onPrimaryContainer", colors.onPrimaryContainer, scheme.onPrimaryContainer)
            assertEquals("$name.secondaryContainer", colors.secondaryContainer, scheme.secondaryContainer)
            assertEquals("$name.onSecondaryContainer", colors.onSecondaryContainer, scheme.onSecondaryContainer)
            assertEquals("$name.surfaceVariant", colors.surfaceContainer, scheme.surfaceVariant)
            assertEquals("$name.onSurfaceVariant", colors.onSurfaceVariant, scheme.onSurfaceVariant)
            assertEquals("$name.surfaceContainerLowest", expectedLowest, scheme.surfaceContainerLowest)
            assertEquals("$name.surfaceContainerLow", colors.surfaceContainerLow, scheme.surfaceContainerLow)
            assertEquals("$name.surfaceContainer", colors.surfaceContainer, scheme.surfaceContainer)
            assertEquals("$name.surfaceContainerHigh", colors.surfaceContainerHigh, scheme.surfaceContainerHigh)
            assertEquals("$name.surfaceContainerHighest", colors.surfaceContainerHigh, scheme.surfaceContainerHighest)
            assertEquals("$name.outline", colors.outline, scheme.outline)
            assertEquals("$name.outlineVariant", colors.outlineVariant, scheme.outlineVariant)

            assertContrastAtLeast("$name onPrimaryContainer/primaryContainer", colors.onPrimaryContainer, colors.primaryContainer)
            assertContrastAtLeast("$name onSecondaryContainer/secondaryContainer", colors.onSecondaryContainer, colors.secondaryContainer)
            assertContrastAtLeast("$name onSurfaceVariant/background", colors.onSurfaceVariant, colors.background)
            assertContrastAtLeast("$name onSurfaceVariant/surface", colors.onSurfaceVariant, colors.surface)
            assertContrastAtLeast("$name onSurfaceVariant/surfaceContainer", colors.onSurfaceVariant, colors.surfaceContainer)
            assertContrastAtLeast("$name onSurface/surfaceContainerLow", colors.onSurface, colors.surfaceContainerLow)
            assertContrastAtLeast("$name onSurface/surfaceContainer", colors.onSurface, colors.surfaceContainer)
            assertContrastAtLeast("$name onSurface/surfaceContainerHigh", colors.onSurface, colors.surfaceContainerHigh)
            assertContrastAtLeast("$name onSecondary/secondary", colors.onSecondary, colors.secondary)
            assertContrastAtLeast("$name onError/error", colors.onError, colors.error)
            assertContrastAtLeast("$name onErrorContainer/errorContainer", colors.onErrorContainer, colors.errorContainer)
            assertContrastAtLeast("$name onSuccess/success", colors.onSuccess, colors.success)
            assertContrastAtLeast("$name snackbar content/container", colors.background, colors.onSurface)
            assertContrastAtLeast("$name snackbar action/container", colors.primaryContainer, colors.onSurface)
        }
    }

    private fun assertContrastAtLeast(label: String, foreground: Color, background: Color) {
        val ratio = contrastRatio(foreground, background)
        assertTrue(
            "$label contrast ratio was %.2f:1, expected >= %.1f:1".format(ratio, WCAG_AA_MIN_CONTRAST),
            ratio >= WCAG_AA_MIN_CONTRAST,
        )
    }

    private fun contrastRatio(first: Color, second: Color): Double {
        val l1 = relativeLuminance(first)
        val l2 = relativeLuminance(second)
        val lighter = max(l1, l2)
        val darker = min(l1, l2)
        return (lighter + CONTRAST_OFFSET) / (darker + CONTRAST_OFFSET)
    }

    private fun relativeLuminance(color: Color): Double {
        val r = linearizeChannel(color.red.toDouble())
        val g = linearizeChannel(color.green.toDouble())
        val b = linearizeChannel(color.blue.toDouble())
        return RED_LUMINANCE_WEIGHT * r + GREEN_LUMINANCE_WEIGHT * g + BLUE_LUMINANCE_WEIGHT * b
    }

    private fun linearizeChannel(channel: Double): Double =
        if (channel <= SRGB_LINEAR_THRESHOLD) {
            channel / SRGB_LINEAR_SCALE
        } else {
            ((channel + SRGB_GAMMA_OFFSET) / SRGB_GAMMA_SCALE).pow(SRGB_GAMMA_EXPONENT)
        }

    private data class PaletteSpec(
        val name: String,
        val palette: SimpleWeatherPalette,
        val background: Color,
        val surface: Color,
        val onSurface: Color,
        val primary: Color,
        val isLight: Boolean,
    )

    companion object {
        private const val WCAG_AA_MIN_CONTRAST = 4.5
        private const val CONTRAST_OFFSET = 0.05
        private const val RED_LUMINANCE_WEIGHT = 0.2126
        private const val GREEN_LUMINANCE_WEIGHT = 0.7152
        private const val BLUE_LUMINANCE_WEIGHT = 0.0722
        private const val SRGB_LINEAR_THRESHOLD = 0.03928
        private const val SRGB_LINEAR_SCALE = 12.92
        private const val SRGB_GAMMA_OFFSET = 0.055
        private const val SRGB_GAMMA_SCALE = 1.055
        private const val SRGB_GAMMA_EXPONENT = 2.4
        private const val MIN_ACCESSIBLE_FONT_SIZE_SP = 12f
        private const val TOTAL_MATERIAL_TEXT_STYLES = 15
    }
}
