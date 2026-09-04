package com.martorell.albert.meteomartocompose.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmDevicePreview
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPreview
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.LocalFndSpacing
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

@Composable
fun TermsScreen(
    modifier: Modifier = Modifier,
    goToLogin: () -> Unit
) {
    // Standard Compose pattern: A single scrollable Column with Arrangement.Center 
    // handles centering when content is small and scrolling when it's large.
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(LocalFndSpacing.current.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.widthIn(max = 600.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            Text(
                modifier = Modifier.padding(top = dimensionResource(R.dimen.standard_height)),
                text = stringResource(R.string.terms_and_conditions),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                modifier = Modifier.padding(dimensionResource(R.dimen.standard_height)),
                text = stringResource(R.string.terms_conditions_content),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            Button(
                onClick = { goToLogin() },
                modifier = Modifier
                    .widthIn(min = 200.dp)
                    .height(dimensionResource(R.dimen.standard_height_button))
            ) {
                Text(
                    text = stringResource(R.string.accept)
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.standard_height)))
        }
    }
}

@MmPreview
@MmDevicePreview
@Composable
private fun TermsScreenPreview() {
    MeteoMartoTheme {
        TermsScreen(goToLogin = {})
    }
}
