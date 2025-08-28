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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.theme.MeteoMartoComposeTheme


@Composable
fun TermsScreen(
    modifier: Modifier = Modifier,
    goToLogin: () -> Unit
) {

    MeteoMartoComposeTheme {

        Scaffold() { innerPadding ->

            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.padding_small),
                    Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = modifier.padding(top = dimensionResource(R.dimen.standard_height)),
                    text = stringResource(R.string.terms_and_conditions),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    modifier = modifier
                        .weight(0.75f)
                        .padding(dimensionResource(R.dimen.standard_height))
                        .verticalScroll(rememberScrollState()),
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

                Spacer(modifier.height(dimensionResource(R.dimen.standard_height)))

            }
        }

    }

}

@Preview
@Composable
fun TermsScreenPreview() {

    TermsScreen(goToLogin = {})

}