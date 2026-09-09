package com.martorell.albert.meteomartocompose.ui.designsystem.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class FndDimensions(
    val buttonMinWidth: Dp = 200.dp,
    val buttonHeight: Dp = 48.dp,
    val authFormWidth: Dp = 300.dp,
    val maxContentWidth: Dp = 600.dp,
)

val LocalFndDimensions = staticCompositionLocalOf { FndDimensions() }
