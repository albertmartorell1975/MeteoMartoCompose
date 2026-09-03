package com.martorell.albert.meteomartocompose.ui.designsystem.foundation

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

object MeteoMartoTheme {
    val spacing: FndSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalFndSpacing.current

    val colors: FndColors
        @Composable
        @ReadOnlyComposable
        get() = LocalFndColors.current

    val typography: FndTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalFndTypography.current
}

@Composable
fun MeteoMartoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    fontScale: Float = UserPreferences.DEFAULT_FONT_SCALE,
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) FndColorsDark else FndColorsLight

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

    FndDensityProvider(userScale = fontScale) {
        CompositionLocalProvider(
            LocalFndSpacing provides FndSpacing(),
            LocalFndColors provides colors,
            LocalFndTypography provides FndTypography(),
        ) {
            MaterialTheme(
                colorScheme = colorScheme,
                content = content
            )
        }
    }
}
