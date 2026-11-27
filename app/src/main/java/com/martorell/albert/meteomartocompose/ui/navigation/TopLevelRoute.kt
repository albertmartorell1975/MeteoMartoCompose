package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/** Immutable descriptor pairing a top-level navigation [route] with its localized label resource and icon. */
@Immutable
data class TopLevelRoute<T : Any>(
    @param:StringRes val titleRes: Int,
    val route: T,
    val icon: ImageVector,
)
