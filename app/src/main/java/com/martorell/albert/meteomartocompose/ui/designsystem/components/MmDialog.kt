package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
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
        Box(
            modifier = Modifier
                .padding(MeteoMartoTheme.spacing.medium)
                .background(
                    color = MeteoMartoTheme.colors.surface,
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(MeteoMartoTheme.spacing.large)
        ) {
            Column {
                MmText.HeadlineMedium("Confirm Action")
                Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))
                MmText.BodyMedium("Are you sure you want to proceed?")
                Spacer(Modifier.height(MeteoMartoTheme.spacing.large))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    MmTertiaryButton(onClick = {}) { MmText.BodyLarge("Cancel") }
                    Spacer(Modifier.width(MeteoMartoTheme.spacing.small))
                    MmSecondaryButton(onClick = {}) { MmText.BodyLarge("Confirm") }
                }
            }
        }
    }
}
