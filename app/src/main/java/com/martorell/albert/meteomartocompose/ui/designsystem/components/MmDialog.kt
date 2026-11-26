package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

/**
 * Themed Material 3 alert dialog styled with MeteoMarto dialog corners and surface tokens.
 *
 * @param onDismissRequest Callback invoked when the user dismisses the dialog via back press or outside tap.
 * @param confirmButton Primary confirmation action composable.
 * @param modifier Modifier applied to the dialog window container.
 * @param dismissButton Optional secondary dismissive action composable.
 * @param icon Optional leading icon displayed above the dialog [title].
 * @param title Optional dialog headline composable.
 * @param text Optional supporting body content composable.
 * @param properties Platform-specific `DialogProperties` controlling dismissal and secure flags.
 */
@Composable
fun MmDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties(),
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        modifier = modifier,
        dismissButton = dismissButton,
        icon = icon,
        title = title,
        text = text,
        shape = MeteoMartoTheme.shapes.dialog,
        containerColor = MeteoMartoTheme.colors.surface,
        iconContentColor = MeteoMartoTheme.colors.primary,
        titleContentColor = MeteoMartoTheme.colors.onSurface,
        textContentColor = MeteoMartoTheme.colors.onSurface,
        properties = properties,
    )
}

/**
 * Inline dialog layout used by design-system previews to render dialog content without
 * spawning a modal window, using wrapping action rows for 200% font-scale safety.
 *
 * @param confirmButton Primary confirmation action composable.
 * @param modifier Modifier applied to the outer column container.
 * @param dismissButton Optional secondary dismissive action composable.
 * @param icon Optional icon rendered centered at the top of the dialog body.
 * @param title Optional dialog headline composable.
 * @param text Optional supporting body content composable.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MmDialogPreviewLayout(
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        icon?.let {
            CompositionLocalProvider(LocalContentColor provides MeteoMartoTheme.colors.primary) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MeteoMartoTheme.spacing.medium),
                    contentAlignment = Alignment.Center,
                ) {
                    it()
                }
            }
        }
        CompositionLocalProvider(LocalContentColor provides MeteoMartoTheme.colors.onSurface) {
            title?.let {
                Box(Modifier.padding(bottom = MeteoMartoTheme.spacing.medium)) { it() }
            }
            text?.let {
                Box(Modifier.padding(bottom = MeteoMartoTheme.spacing.large)) { it() }
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = MeteoMartoTheme.spacing.small,
                    alignment = Alignment.End,
                ),
                verticalArrangement = Arrangement.spacedBy(MeteoMartoTheme.spacing.small),
            ) {
                dismissButton?.invoke()
                confirmButton()
            }
        }
    }
}

@MmPreview
@Composable
private fun MmDialogPreview() {
    MeteoMartoTheme {
        Surface(color = MeteoMartoTheme.colors.background) {
            Surface(
                shape = MeteoMartoTheme.shapes.dialog,
                color = MeteoMartoTheme.colors.surface,
                modifier = Modifier.padding(MeteoMartoTheme.spacing.medium),
            ) {
                MmDialogPreviewLayout(
                    modifier = Modifier.padding(MeteoMartoTheme.spacing.large),
                    title = { MmText.HeadlineMedium("Confirm Action") },
                    text = { MmText.BodyMedium("Are you sure you want to proceed?") },
                    confirmButton = {
                        MmSecondaryButton(onClick = {}) { MmText.LabelLarge("Confirm") }
                    },
                    dismissButton = {
                        MmTertiaryButton(onClick = {}) { MmText.LabelLarge("Cancel") }
                    },
                )
            }
        }
    }
}

@MmDynamicPalettesPreview
@Composable
private fun MmDialogPalettesPreview() {
    MmDynamicPalettesShowcase {
        Surface(
            shape = MeteoMartoTheme.shapes.dialog,
            color = MeteoMartoTheme.colors.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(MeteoMartoTheme.spacing.medium),
        ) {
            MmDialogPreviewLayout(
                modifier = Modifier.padding(MeteoMartoTheme.spacing.large),
                title = { MmText.TitleLarge("Log out") },
                text = { MmText.BodyMedium("Are you sure you want to log out?") },
                confirmButton = {
                    MmSecondaryButton(onClick = {}) { MmText.LabelLarge("Continue") }
                },
                dismissButton = {
                    MmTertiaryButton(onClick = {}) { MmText.LabelLarge("Cancel") }
                },
            )
        }
    }
}
