package com.martorell.albert.meteomartocompose.ui.designsystem.foundation

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
 * FndDesignSystemViewModel handles Design System logic (accessibility, scaling).
 */
@HiltViewModel
class FndDesignSystemViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) : ViewModel() {

    companion object {
        private const val FLOW_STOP_TIMEOUT = 5000L
    }

    val fontScale: StateFlow<Float> = userPreferences.fontScale
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(FLOW_STOP_TIMEOUT),
            initialValue = UserPreferences.DEFAULT_FONT_SCALE
        )

    fun updateFontScale(scale: Float) {
        viewModelScope.launch {
            userPreferences.saveFontScale(scale)
        }
    }
}
