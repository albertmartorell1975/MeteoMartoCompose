package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

@Composable
fun MmDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        modifier = modifier,
        dismissButton = dismissButton,
        title = title,
        text = text,
        containerColor = MeteoMartoTheme.colors.surface,
        titleContentColor = MeteoMartoTheme.colors.onSurface,
        textContentColor = MeteoMartoTheme.colors.onSurface
    )
}

@MmPreview
@Composable
private fun MmDialogPreview() {
    MeteoMartoTheme {
        MmDialog(
            onDismissRequest = {},
            title = { MmText.HeadlineMedium("Confirm Action") },
            text = { MmText.BodyMedium("Are you sure you want to proceed?") },
            confirmButton = {
                MmSecondaryButton(onClick = {}) { MmText.BodyLarge("Confirm") }
            },
            dismissButton = {
                MmTertiaryButton(onClick = {}) { MmText.BodyLarge("Cancel") }
            }
        )
    }
}
