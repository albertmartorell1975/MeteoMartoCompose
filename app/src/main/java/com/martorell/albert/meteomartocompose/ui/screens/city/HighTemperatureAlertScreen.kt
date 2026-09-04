package com.martorell.albert.meteomartocompose.ui.screens.city

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPreview
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.LocalFndSpacing
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
            .background(MaterialTheme.colorScheme.errorContainer)
            .safeDrawingPadding()
            .padding(LocalFndSpacing.current.medium)
    ) {
        IconButton(
            onClick = onDismiss,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.padding(bottom = LocalFndSpacing.current.medium),
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = stringResource(R.string.high_temp_notif_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.high_temp_notif_content, temperature),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(
                    top = LocalFndSpacing.current.medium,
                    bottom = LocalFndSpacing.current.extraLarge
                )
            )
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text(text = stringResource(R.string.accept_warning))
            }
        }
    }
}

@MmPreview
@Composable
private fun HighTemperatureAlertScreenPreview() {
    MeteoMartoTheme {
        HighTemperatureAlertScreen(
            temperature = 35.5,
            onDismiss = {}
        )
    }
}

