package com.martorell.albert.meteomartocompose.ui.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * UI-layer abstraction for display text that decouples ViewModels from Android `Context` and
 * `R.string` resolution.
 */
sealed interface UiText {

    /**
     * Plain string value that does not require Android resource resolution.
     *
     * @property value Raw display string.
     */
    data class DynamicString(val value: String) : UiText

    /**
     * Localized string resource reference resolved at the UI boundary.
     *
     * @property resId Android string resource identifier.
     */
    data class StringResource(@param:StringRes @get:StringRes val resId: Int) : UiText

    /**
     * Resolves this [UiText] into a [String] within a Jetpack Compose composition.
     */
    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> value
        is StringResource -> stringResource(resId)
    }

    /**
     * Resolves this [UiText] into a [String] using the provided Android [context].
     */
    fun asString(context: Context): String = when (this) {
        is DynamicString -> value
        is StringResource -> context.getString(resId)
    }
}
