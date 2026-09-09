package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        dismissButton = dismissButton,
        title = title,
        text = text,
        modifier = modifier,
        containerColor = MeteoMartoTheme.colors.surface,
        titleContentColor = MeteoMartoTheme.colors.onSurface,
        textContentColor = MeteoMartoTheme.colors.onSurface
    )
}

/**
 * Internal layout that mimics the AlertDialog structure. 
 * Used for Previews and Roborazzi snapshots to avoid multi-window capture issues.
 */
@Composable
internal fun MmDialogPreviewLayout(
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
) {
    Column {
        CompositionLocalProvider(LocalContentColor provides MeteoMartoTheme.colors.onSurface) {
            title?.let {
                Box(Modifier.padding(bottom = MeteoMartoTheme.spacing.medium)) { it() }
            }
            text?.let {
                Box(Modifier.padding(bottom = MeteoMartoTheme.spacing.large)) { it() }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                dismissButton?.let {
                    it()
                    Spacer(Modifier.width(MeteoMartoTheme.spacing.small))
                }
                confirmButton()
            }
        }
    }
}

@MmPreview
@Composable
private fun MmDialogPreview() {
    MeteoMartoTheme {
        Surface(
            shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp),
            color = MeteoMartoTheme.colors.surface,
            modifier = Modifier.padding(MeteoMartoTheme.spacing.medium)
        ) {
            Box(Modifier.padding(MeteoMartoTheme.spacing.large)) {
                MmDialogPreviewLayout(
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
    }
}
