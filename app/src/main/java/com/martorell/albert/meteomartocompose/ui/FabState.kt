package com.martorell.albert.meteomartocompose.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Manages the state and interactions of the Floating Action Button (FAB).
 */
class FabState(
    private val coroutineScope: CoroutineScope
) {
    var isVisible by mutableStateOf(value = false)
        private set

    private var userClickedCount by mutableIntStateOf(0)

    fun updateVisibility(newValue: Boolean) {
        isVisible = newValue
    }

    @Composable
    fun getIcon(isCityFavoriteAction: suspend () -> Boolean): ImageVector? {
        val icon by produceState<ImageVector?>(
            initialValue = null,
            key1 = userClickedCount
        ) {
            value = if (isCityFavoriteAction.invoke()) {
                Icons.Default.Favorite
            } else {
                Icons.Default.FavoriteBorder
            }
        }
        return icon
    }

    fun onFabClick(action: () -> Unit) {
        coroutineScope.launch { action.invoke() }
        userClickedCount += 1
    }
}
