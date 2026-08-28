package com.martorell.albert.meteomartocompose.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martorell.albert.meteomartocompose.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing Design System state, such as accessibility preferences.
 */
@HiltViewModel
class DesignSystemViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) : ViewModel() {

    companion object {
        /**
         * Keeps the flow active for 5 seconds after the last subscriber leaves.
         * This prevents restarting the flow during configuration changes (like rotations).
         */
        private const val FLOW_STOP_TIMEOUT = 5000L
    }

    /**
     * Reactively exposes the user-defined font scale from DataStore.
     */
    val fontScale: StateFlow<Float> = userPreferences.fontScale
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(FLOW_STOP_TIMEOUT),
            initialValue = UserPreferences.DEFAULT_FONT_SCALE
        )

    /**
     * Updates the font scale in the persistent DataStore.
     */
    fun updateFontScale(scale: Float) {
        viewModelScope.launch {
            userPreferences.saveFontScale(scale)
        }
    }
}
