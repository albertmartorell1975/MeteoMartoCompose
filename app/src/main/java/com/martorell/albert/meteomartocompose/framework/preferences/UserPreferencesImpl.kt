package com.martorell.albert.meteomartocompose.framework.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.martorell.albert.meteomartocompose.data.preferences.UserPreferences
import com.martorell.albert.meteomartocompose.utils.AppConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val USER_PREFERENCES_NAME = "user_preferences"

/**
 * From here, any Context into this file will have a property named dataStore, managed by the preferencesDataStore delegate
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_NAME)

/**
 * Implementation of UserPreferences using Jetpack DataStore.
 * Responsible for persisting user UI preferences like font scaling.
 */
@Singleton
class UserPreferencesImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : UserPreferences {

    private object PreferencesKeys {
        val FONT_SCALE = floatPreferencesKey("font_scale")
    }

    override val fontScale: Flow<Float> = context.dataStore.data
        .catch { exception ->
            // In a Design System, we must never crash due to a preference read failure.
            // We log the error and fallback to empty preferences, which triggers the DEFAULT_FONT_SCALE.
            Log.e(AppConstants.DEBUG_TAG, "Error reading user preferences", exception)
            emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferencesKeys.FONT_SCALE] ?: UserPreferences.DEFAULT_FONT_SCALE
        }

    override suspend fun saveFontScale(scale: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_SCALE] = scale
        }
    }
}