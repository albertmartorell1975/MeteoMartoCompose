package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

/**
 * Declarative side-effect helper that displays a snackbar in [hostState] whenever [message],
 * [actionLabel], or [key] changes, and dispatches the resulting user interaction callback.
 *
 * @param hostState `SnackbarHostState` coordinating queued snackbars.
 * @param message Text message displayed inside the snackbar.
 * @param actionLabel Optional action button label.
 * @param duration Display duration before automatic dismissal.
 * @param onActionPerformed Callback invoked when the user taps the snackbar action.
 * @param onDismissed Callback invoked when the snackbar is dismissed without action.
 * @param key Additional effect keys that trigger re-displaying the snackbar when changed.
 */
@Composable
fun MmSnackbar(
    hostState: SnackbarHostState,
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onActionPerformed: () -> Unit = {},
    onDismissed: () -> Unit = {},
    vararg key: Any,
) {
    val currentOnActionPerformed by rememberUpdatedState(onActionPerformed)
    val currentOnDismissed by rememberUpdatedState(onDismissed)

    LaunchedEffect(message, actionLabel, *key) {
        val result = hostState.showMmSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = duration,
        )
        when (result) {
            SnackbarResult.ActionPerformed -> currentOnActionPerformed()
            SnackbarResult.Dismissed -> currentOnDismissed()
        }
    }
}

/**
 * Themed Material 3 `SnackbarHost` using high-contrast inverted surface tokens and
 * [MeteoMartoTheme.shapes] input corners.
 *
 * @param hostState `SnackbarHostState` driving displayed snackbar entries.
 * @param modifier Modifier applied to the `SnackbarHost`.
 */
@Composable
fun MmSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
    ) { snackbarData ->
        Snackbar(
            snackbarData = snackbarData,
            shape = MeteoMartoTheme.shapes.input,
            containerColor = MeteoMartoTheme.colors.onSurface,
            contentColor = MeteoMartoTheme.colors.background,
            actionColor = MeteoMartoTheme.colors.primaryContainer,
            actionContentColor = MeteoMartoTheme.colors.primaryContainer,
            dismissActionContentColor = MeteoMartoTheme.colors.background,
        )
    }
}

/**
 * Suspends to display a snackbar on this `SnackbarHostState` with MeteoMarto defaults.
 *
 * @param message Text message displayed inside the snackbar.
 * @param actionLabel Optional action button label.
 * @param duration Display duration before automatic dismissal.
 * @return `SnackbarResult` indicating whether the action was performed or the snackbar was dismissed.
 */
suspend fun SnackbarHostState.showMmSnackbar(
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
): SnackbarResult {
    return showSnackbar(
        message = message,
        actionLabel = actionLabel,
        duration = duration,
    )
}

@Composable
private fun MmSnackbarSample() {
    Snackbar(
        modifier = Modifier.padding(MeteoMartoTheme.spacing.medium),
        action = {
            TextButton(
                onClick = {},
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MeteoMartoTheme.colors.primaryContainer,
                ),
            ) {
                MmText.LabelLarge(text = "Accept")
            }
        },
        shape = MeteoMartoTheme.shapes.input,
        containerColor = MeteoMartoTheme.colors.onSurface,
        contentColor = MeteoMartoTheme.colors.background,
        actionContentColor = MeteoMartoTheme.colors.primaryContainer,
    ) {
        MmText.BodyMedium(text = "Unable to connect. Please check your network.")
    }
}

@MmPreview
@Composable
private fun MmSnackbarPreview() {
    MeteoMartoTheme {
        Surface(color = MeteoMartoTheme.colors.background) {
            MmSnackbarSample()
        }
    }
}

@MmDynamicPalettesPreview
@Composable
private fun MmSnackbarPalettesPreview() {
    MmDynamicPalettesShowcase {
        MmSnackbarSample()
    }
}
