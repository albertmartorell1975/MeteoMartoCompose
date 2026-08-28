package com.martorell.albert.meteomartocompose.data.preferences

import kotlinx.coroutines.flow.Flow

/**
 * Interface for user-specific infrastructure preferences.
 * Defined in :data to allow Domain/UseCase access to data contracts.
 */
interface UserPreferences {
    /**
     * Flow that emits the current font scaling factor.
     */
    val fontScale: Flow<Float>

    /**
     * Persists a new font scaling factor.
     */
    suspend fun saveFontScale(scale: Float)

    companion object {
        /**
         * The standard 100% scale reference.
         * Used as initial value and fallback when no preferences are saved.
         */
        const val DEFAULT_FONT_SCALE = 1.0f
    }
}