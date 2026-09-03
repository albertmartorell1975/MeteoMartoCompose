package com.martorell.albert.meteomartocompose.ui.mappers

import androidx.annotation.StringRes
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.data.CustomError

/**
 * Maps domain-level [CustomError] to Android string resource IDs.
 * This ensures that the UI layer remains decoupled from the domain logic
 * while providing a centralized translation mechanism.
 */
@StringRes
fun CustomError.asStringRes(): Int {
    return when (this) {
        is CustomError.Connectivity -> R.string.error_connectivity
        is CustomError.FirebaseError -> R.string.error_firebase
        is CustomError.Unknown -> R.string.error_unknown
    }
}
