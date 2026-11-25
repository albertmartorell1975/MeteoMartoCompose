package com.martorell.albert.meteomartocompose.ui.designsystem.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Layout dimension and sizing tokens for MeteoMarto components.
 *
 * @property buttonMinWidth Minimum width for standard pill action buttons.
 * @property buttonHeight Minimum height enforcing accessible 48.dp touch targets on buttons.
 * @property authFormWidth Recommended maximum width for authentication form fields.
 * @property maxContentWidth Maximum content width applied on expanded screens and error states.
 * @property alertIconSize Icon size for high-temperature alert banners.
 * @property weatherIconSize Fixed square size for remote weather condition icons.
 * @property errorIconSize Illustration size displayed inside [MmErrorState][com.martorell.albert.meteomartocompose.ui.designsystem.components.MmErrorState].
 * @property borderStrokeWidth Standard stroke width for outlined card and container borders.
 */
@Immutable
data class FndDimensions(
    val buttonMinWidth: Dp = 200.dp,
    val buttonHeight: Dp = 48.dp,
    val authFormWidth: Dp = 300.dp,
    val maxContentWidth: Dp = 600.dp,
    val alertIconSize: Dp = 80.dp,
    val weatherIconSize: Dp = 90.dp,
    val errorIconSize: Dp = 120.dp,
    val borderStrokeWidth: Dp = 1.dp,
)

/**
 * CompositionLocal providing the active [FndDimensions] tokens.
 */
val LocalFndDimensions = staticCompositionLocalOf { FndDimensions() }
