package com.martorell.albert.meteomartocompose.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import com.martorell.albert.meteomartocompose.data.preferences.UserPreferences

/**
 * MeteoMartoTheme provides a central access point for all tokens.
 * Usage: MeteoMartoTheme.spacing.medium
 */
object MeteoMartoTheme {
    val spacing: MMSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalMMSpacing.current

    val colors: MMColors
        @Composable
        @ReadOnlyComposable
        get() = LocalMMColors.current

    val typography: MMTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalMMTypography.current
}

/**
 * Main Theme wrapper for the MeteoMarto Design System.
 * It coordinates custom tokens, dynamic density scaling, and Material 3 interoperability.
 *
 * @param darkTheme Whether to use the dark color palette.
 * @param dynamicColor Whether to use Android 12+ dynamic color (Material You).
 * @param fontScale The user-defined font scaling factor (pinch-to-zoom).
 * @param content The UI tree to be themed.
 */
@Composable
fun MeteoMartoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    fontScale: Float = UserPreferences.DEFAULT_FONT_SCALE,
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) MMColorsDark else MMColorsLight

    // Bridge custom MMColors to Material 3 ColorScheme for interoperability
    val colorScheme = when {
        ((dynamicColor && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S))) -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme(
            primary = colors.primary,
            onPrimary = colors.onPrimary,
            secondary = colors.secondary,
            onSecondary = colors.onSecondary,
            surface = colors.surface,
            onSurface = colors.onSurface,
            error = colors.error,
            onError = colors.onError,
        )

        else -> lightColorScheme(
            primary = colors.primary,
            onPrimary = colors.onPrimary,
            secondary = colors.secondary,
            onSecondary = colors.onSecondary,
            surface = colors.surface,
            onSurface = colors.onSurface,
            error = colors.error,
            onError = colors.onError
        )
    }

    MMDensityProvider(userScale = fontScale) {
        CompositionLocalProvider(
            LocalMMSpacing provides MMSpacing(),
            LocalMMColors provides colors,
            LocalMMTypography provides MMTypography(),
        ) {
            MaterialTheme(
                colorScheme = colorScheme,
                // Map MMTypography to M3 Typography roles in Phase 3
                content = content
            )
        }
    }
}
