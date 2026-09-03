package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

@Composable
fun MmErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    retryLabel: String = "Try Again",
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MeteoMartoTheme.spacing.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            modifier = Modifier.size(128.dp),
            tint = MeteoMartoTheme.colors.error
        )

        Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))

        MmText.HeadlineMedium(
            text = message,
            textAlign = TextAlign.Center,
            color = MeteoMartoTheme.colors.onSurface
        )

        Spacer(Modifier.height(MeteoMartoTheme.spacing.large))

        MmPrimaryButton(onClick = onRetry) {
            MmText.BodyLarge(text = retryLabel)
        }
    }
}

@MmPreview
@Composable
private fun MmErrorStatePreview() {
    MeteoMartoTheme {
        MmErrorState(
            message = "Something went wrong.",
            onRetry = {}
        )
    }
}
