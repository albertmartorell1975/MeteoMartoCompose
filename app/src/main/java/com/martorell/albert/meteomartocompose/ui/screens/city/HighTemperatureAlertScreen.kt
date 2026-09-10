package com.martorell.albert.meteomartocompose.ui.screens.city

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmDevicePreview
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPrimaryButton
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmText
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPreview
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

@Composable
fun HighTemperatureAlertScreen(
    temperature: Double,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MeteoMartoTheme.colors.errorContainer)
            .safeDrawingPadding()
            .padding(MeteoMartoTheme.spacing.medium),
    ) {
        IconButton(
            onClick = onDismiss,
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.back),
                tint = MeteoMartoTheme.colors.onErrorContainer
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier
                    .size(MeteoMartoTheme.dimensions.alertIconSize)
                    .padding(bottom = MeteoMartoTheme.spacing.medium),
                tint = MeteoMartoTheme.colors.error,
            )
            MmText.HeadlineMedium(
                text = stringResource(R.string.high_temp_notif_title),
                color = MeteoMartoTheme.colors.onErrorContainer,
                textAlign = TextAlign.Center,
            )
            MmText.BodyLarge(
                text = stringResource(R.string.high_temp_notif_content, temperature),
                color = MeteoMartoTheme.colors.onErrorContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(
                    top = MeteoMartoTheme.spacing.medium,
                    bottom = MeteoMartoTheme.spacing.extraLarge,
                ),
            )
            MmPrimaryButton(
                onClick = onDismiss,
                modifier = Modifier.widthIn(max = MeteoMartoTheme.dimensions.authFormWidth),
            ) {
                MmText.BodyLarge(text = stringResource(R.string.accept_warning))
            }
        }
    }
}

@MmPreview
@MmDevicePreview
@Composable
private fun HighTemperatureAlertScreenPreview() {
    MeteoMartoTheme {
        HighTemperatureAlertScreen(
            temperature = 35.5,
            onDismiss = {}
        )
    }
}