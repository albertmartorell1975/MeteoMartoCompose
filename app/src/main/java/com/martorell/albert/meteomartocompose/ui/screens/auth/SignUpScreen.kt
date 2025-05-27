package com.martorell.albert.meteomartocompose.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.MeteoMartoComposeLayout

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    goToLogin: () -> Unit
) {

    MeteoMartoComposeLayout {

        Scaffold { innerPadding ->

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.padding_small), Alignment.CenterVertically
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = stringResource(R.string.sign_up_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier.height(dimensionResource(R.dimen.standard_height)))
                    TextField(
                        value = "",
                        onValueChange = {

                        },
                        label = { Text(text = stringResource(R.string.label_user_text_field)) },
                        placeholder = { Text(text = stringResource(R.string.placeholder_user_text_field)) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text
                        ),

                        )

                    TextField(
                        value = "",
                        onValueChange = {

                        },
                        label = { Text(text = stringResource(R.string.label_password_text_field)) },
                        placeholder = { Text(text = stringResource(R.string.placeholder_password_text_field)) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Password
                        )
                    )

                    Spacer(modifier.height(dimensionResource(R.dimen.standard_height)))

                    Button(
                        modifier = Modifier
                            .widthIn(min = 200.dp)
                            .height(dimensionResource(R.dimen.standard_height_button)),
                        onClick = {}
                    ) {
                        Text(text = stringResource(R.string.sign_up))
                    }

                    Spacer(modifier.height(dimensionResource(R.dimen.standard_height)))

                }

            }

        }

    }

}

@Preview
@Composable
fun SignUpPreview() {

    SignUpScreen(goToLogin = {})

}
