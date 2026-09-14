package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/**
 * MmSnackbar - The official Snackbar strategy for MeteoMarto.
 *
 * This file provides two ways to interact with snackbars because Compose strictly separates 
 * "Drawing UI" from "Executing Actions":
 *
 * 1. Declarative (@Composable): 
 *    - WHEN: For messages tied to a UI State (e.g., "Offline Mode").
 *    - WHY: It belongs to the composition tree. If the screen recomposes, the UI state 
 *      dictates if the snackbar stays or leaves.
 *
 * 2. Imperative (suspend extension): 
 *    - WHEN: For one-shot events triggered by actions (Errors, confirmations).
 *    - WHY: Events are not state. Using a suspend function avoids "state-clearing boilerplate" 
 *      (e.g., having to manually reset a 'showSnackbar' flag after it disappears) and 
 *      respects the async nature of LaunchedEffect.
 */

@Composable
fun MmSnackbar(
    hostState: SnackbarHostState,
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onActionPerformed: () -> Unit = {},
    onDismissed: () -> Unit = {},
    vararg key: Any
) {
    LaunchedEffect(message, actionLabel, *key) {
        val result = hostState.showMmSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = duration
        )
        when (result) {
            SnackbarResult.ActionPerformed -> onActionPerformed()
            SnackbarResult.Dismissed -> onDismissed()
        }
    }
}

/**
 * Imperative extension to be used within LaunchedEffect or any CoroutineScope.
 * This is the preferred way to show Snackbars triggered by ViewModel events.
 */
suspend fun SnackbarHostState.showMmSnackbar(
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short
): SnackbarResult {
    return showSnackbar(
        message = message,
        actionLabel = actionLabel,
        duration = duration
    )
}
