package com.martorell.albert.meteomartocompose.ui.screens.shared

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Custom Snack Bar . Its goal is to be re-used and avoid boilerplate code
 * @param title the given title string
 * @param action the given action string
 * @param coroutineScope gives us a safe way to launch coroutines from within the composable functions without
 * worrying about memory leaks or  wasting resources on background tasks that are no longer needed.
 * @param performAction the action (could be a coroutine) to be done when user clicks on action button
 * @param performDismissed the action (could be a coroutine) to be release when SnackBar is dismissed
 * @param key: a brunch of arguments that we do not know how many of them are
 */
@Composable
fun SnackbarHostState.SnackBarCustom(
    @StringRes title: Int,
    @StringRes action: Int,
    coroutineScope: CoroutineScope,
    performAction: () -> Unit,
    performDismissed: () -> Unit,
    vararg key: Any
) {

    val titleText = LocalContext.current.getText(title).toString()
    val actionText = LocalContext.current.getText(action).toString()

    //"*key" means that will ensure that it can be passed to a vararg method again
    LaunchedEffect(*key) {

        coroutineScope.launch {

            val result = this@SnackBarCustom.showSnackbar(
                duration = SnackbarDuration.Long,
                message = titleText,
                actionLabel = actionText
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    performAction()
                }

                SnackbarResult.Dismissed -> {
                    performDismissed()
                }

            }
        }

    }

}