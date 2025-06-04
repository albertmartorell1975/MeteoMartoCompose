package com.martorell.albert.meteomartocompose.ui.navigation.shared

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class TopLevelRoute<T : Any>( @StringRes val title: Int, val route: T, @Contextual val icon: ImageVector)