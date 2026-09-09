package com.martorell.albert.meteomartocompose.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmDevicePreview
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPrimaryButton
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPreview
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmText
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

@Composable
fun TermsScreen(
    modifier: Modifier = Modifier,
    goToLogin: () -> Unit
) {
    TermsContent(
        modifier = modifier,
        onAcceptClick = goToLogin
    )
}

@Composable
fun TermsContent(
    modifier: Modifier = Modifier,
    onAcceptClick: () -> Unit
) {
    // El Box actua com a contenidor arrel per centrar la columna a la pantalla.
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Una única Column que gestiona el scroll, l'amplada màxima i l'espaiat.
        Column(
            modifier = Modifier
                .widthIn(max = MeteoMartoTheme.dimensions.maxContentWidth)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(MeteoMartoTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                MeteoMartoTheme.spacing.small,
                Alignment.CenterVertically
            )
        ) {
            MmText.TitleLarge(
                modifier = Modifier.padding(top = MeteoMartoTheme.spacing.large),
                text = stringResource(R.string.terms_and_conditions),
                color = MeteoMartoTheme.colors.primary
            )

            MmText.BodyLarge(
                modifier = Modifier.padding(vertical = MeteoMartoTheme.spacing.large),
                text = stringResource(R.string.terms_conditions_content),
                color = MeteoMartoTheme.colors.secondary
            )

            MmPrimaryButton(
                onClick = onAcceptClick,
                modifier = Modifier
                    .widthIn(min = MeteoMartoTheme.dimensions.buttonMinWidth)
                    .height(MeteoMartoTheme.dimensions.buttonHeight)
            ) {
                MmText.BodyMedium(
                    text = stringResource(R.string.accept)
                )
            }

            Spacer(modifier = Modifier.height(MeteoMartoTheme.spacing.large))
        }
    }
}

@MmPreview
@MmDevicePreview
@Composable
private fun TermsScreenPreview() {
    MeteoMartoTheme {
        TermsContent(onAcceptClick = {})
    }
}
