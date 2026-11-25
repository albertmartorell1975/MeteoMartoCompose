package com.martorell.albert.meteomartocompose.ui.designsystem.foundation

import androidx.compose.ui.graphics.Color

/**
 * Single-source raw hex color primitives used by weather palettes and [FndColors].
 *
 * Primitives are grouped by palette family (DayBaseline, Sunny, Rain, Night, ExtremeAlert),
 * followed by shared semantic error, success, and thermal accent values.
 */
internal object FndColorPrimitives {
    val DayBaselineBackground = Color(0xFFF8F9FA)
    val DayBaselineOnSurface = Color(0xFF1A1C1E)
    val DayBaselinePrimary = Color(0xFF4A627A)
    val DayBaselineSecondary = Color(0xFF53606E)
    val SlateContainerLight = Color(0xFFD6E4F0)
    val DayBaselineOnPrimaryContainer = Color(0xFF132638)
    val DayBaselineSecondaryContainer = Color(0xFFE1E8EF)
    val DayBaselineOnSecondaryContainer = Color(0xFF1B2836)
    val DayBaselineOutline = Color(0xFF737D87)
    val DayBaselineOutlineVariant = Color(0xFFC4CCD4)
    val DayBaselineSurfaceContainerLow = Color(0xFFF3F5F8)
    val DayBaselineSurfaceContainer = Color(0xFFECEFF3)
    val DayBaselineSurfaceContainerHigh = Color(0xFFE4E8EE)

    val SunnyBackground = Color(0xFFFFF8F0)
    val SunnyOnSurface = Color(0xFF2D2314)
    val SunnyPrimary = Color(0xFFE68A00)
    val SunnySecondary = Color(0xFF6E5D40)
    val SunnyPrimaryContainer = Color(0xFFFFE0B2)
    val SunnySecondaryContainer = Color(0xFFF5E6D3)
    val SunnyOutline = Color(0xFF8C7A5B)
    val SunnyOutlineVariant = Color(0xFFDECBB1)
    val SunnySurfaceContainerLow = Color(0xFFFCF2E6)
    val SunnySurfaceContainer = Color(0xFFF7EBDC)
    val SunnySurfaceContainerHigh = Color(0xFFF0E0CD)

    val RainBackground = Color(0xFFEBF2F7)
    val RainSurface = Color(0xFFF4F8FB)
    val RainOnSurface = Color(0xFF13202C)
    val RainPrimary = Color(0xFF2C5282)
    val RainSecondary = Color(0xFF47607A)
    val RainPrimaryContainer = Color(0xFFCDE0F5)
    val RainOutline = Color(0xFF697F96)
    val RainOutlineVariant = Color(0xFFBCCCDD)
    val RainSurfaceContainerLow = Color(0xFFE5EEF5)
    val RainSurfaceContainer = Color(0xFFDCE7F0)
    val RainSurfaceContainerHigh = Color(0xFFD2E0EC)

    val NightBackground = Color(0xFF0E1419)
    val NightSurface = Color(0xFF161C21)
    val NightOnSurface = Color(0xFFE1E2E5)
    val NightPrimary = Color(0xFF9D8DF1)
    val NightSecondary = Color(0xFFB8C4D0)
    val NightPrimaryContainer = Color(0xFF332B66)
    val NightOnPrimaryContainer = Color(0xFFEAE4FF)
    val NightSecondaryContainer = Color(0xFF26303A)
    val NightOutline = Color(0xFF8E9196)
    val NightOutlineVariant = Color(0xFF44474C)
    val NightSurfaceContainer = Color(0xFF1A2025)
    val NightSurfaceContainerHigh = Color(0xFF242B30)

    val ExtremeAlertBackground = Color(0xFF701A1A)
    val ExtremeAlertSurface = Color(0xFF8B2323)
    val ExtremeAlertPrimary = Color(0xFFB5C4FF)
    val ExtremeAlertPrimaryContainer = Color(0xFF263B7A)
    val ExtremeAlertOutlineVariant = Color(0xFFB34B4B)
    val ExtremeAlertSurfaceContainerLow = Color(0xFF7E1E1E)
    val ExtremeAlertSurfaceContainerHigh = Color(0xFF9E2D2D)

    val ErrorLight = Color(0xFFBA1A1A)
    val ErrorContainerLight = Color(0xFFFFDAD6)
    val OnErrorContainerLight = Color(0xFF410002)
    val ErrorDark = Color(0xFFFFB4AB)
    val OnErrorDark = Color(0xFF690005)
    val ErrorContainerDark = Color(0xFF93000A)
    val ErrorContainerAlert = Color(0xFF530F0F)

    val SuccessLight = Color(0xFF2E7D32)
    val SuccessDark = Color(0xFF81C784)
    val SuccessAlert = Color(0xFFA5D6A7)
    val OnSuccessDark = Color(0xFF003300)

    val ThermalHotLight = Color(0xFFD32F2F)
    val ThermalColdLight = Color(0xFF1976D2)
    val ThermalColdRain = Color(0xFF1565C0)
    val ThermalHotNight = Color(0xFFFF8A80)
    val ThermalColdNight = Color(0xFF82B1FF)
}
