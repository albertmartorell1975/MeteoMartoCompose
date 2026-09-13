package com.martorell.albert.meteomartocompose.ui.mappers

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.data.CustomErrorFlow

/**
 * Mappers to transform domain/data errors into localized UI messages.
 */

@Composable
fun CustomError.toMessage(): String = when (this) {
    CustomError.Connectivity -> stringResource(R.string.error_connectivity)
    is CustomError.FirebaseError -> stringResource(R.string.error_firebase)
    is CustomError.InvalidCredentials -> stringResource(R.string.error_invalid_credentials)
    is CustomError.Unknown -> this.message.ifEmpty { stringResource(R.string.error_unknown) }
}

fun CustomError.toMessage(context: Context): String = when (this) {
    CustomError.Connectivity -> context.getString(R.string.error_connectivity)
    is CustomError.FirebaseError -> context.getString(R.string.error_firebase)
    is CustomError.InvalidCredentials -> context.getString(R.string.error_invalid_credentials)
    is CustomError.Unknown -> this.message.ifEmpty { context.getString(R.string.error_unknown) }
}

@Composable
fun CustomErrorFlow.toMessage(): String = when (this) {
    CustomErrorFlow.Connectivity -> stringResource(R.string.error_connectivity)
    is CustomErrorFlow.Server -> stringResource(R.string.error_firebase)
    is CustomErrorFlow.Unknown -> this.message.ifEmpty { stringResource(R.string.error_unknown) }
}
