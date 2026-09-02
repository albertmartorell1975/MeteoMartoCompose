package com.martorell.albert.meteomartocompose.ui.screens.shared

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.Modifier

@Composable
fun AlertDialogCustom(
    @StringRes title: Int,
    @StringRes content: Int,
    @StringRes actionText: Int,
    @StringRes dismissText: Int,
    onDismissAction: () -> Unit,
    onConfirmAction: () -> Unit,
    modifier: Modifier = Modifier,
) {

    AlertDialog(
        onDismissRequest = { onDismissAction() },
        modifier = modifier,
        title = { Text(stringResource(id = title)) },
        text = {
            Text(stringResource(id = content))
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmAction()
            }) {
                Text(stringResource(id = actionText))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissAction() }) {
                Text(stringResource(id = dismissText))
            }
        })

}