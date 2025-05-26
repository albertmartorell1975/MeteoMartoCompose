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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.MeteoMartoComposeLayout
import com.martorell.albert.meteomartocompose.ui.screens.shared.MeteoMartoCircularProgressIndicator

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    goToTerms: () -> Unit,
    goToDashboard: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>()
) {

    val state = viewModel.state.collectAsState()
    val userTextState = remember { mutableStateOf(TextFieldValue()) }
    val passwordTextState = remember { mutableStateOf(TextFieldValue()) }
    var passwordVisible by remember { mutableStateOf(false) }
    val loginEnabled =
        passwordTextState.value.text.isNotEmpty() &&
                userTextState.value.text.isNotEmpty()
    val keyboardController = LocalSoftwareKeyboardController.current

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
                        text = stringResource(R.string.login_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier.height(dimensionResource(R.dimen.standard_height)))
                    TextField(
                        value = userTextState.value,
                        onValueChange = {
                            userTextState.value = it
                            viewModel.loginUnchecked()
                        },
                        label = { Text(text = stringResource(R.string.label_user_text_field)) },
                        placeholder = { Text(text = stringResource(R.string.placeholder_user_text_field)) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text
                        ),
                        isError = !state.value.loginStatus && state.value.loginChecked
                    )

                    TextField(
                        value = passwordTextState.value,
                        onValueChange = {
                            passwordTextState.value = it
                            viewModel.loginUnchecked()
                        },
                        label = { Text(text = stringResource(R.string.label_password_text_field)) },
                        placeholder = { Text(text = stringResource(R.string.placeholder_password_text_field)) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation = if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconToggleButton(
                                checked = passwordVisible,
                                onCheckedChange = { passwordVisible = it }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Default.VisibilityOff
                                    else Icons.Default.Visibility,
                                    contentDescription = stringResource(R.string.visibility_password)
                                )
                            }
                        },
                        isError = !state.value.loginStatus && state.value.loginChecked
                    )

                    Spacer(modifier.height(dimensionResource(R.dimen.standard_height)))

                    if (state.value.loginChecked)
                        if (state.value.loginStatus)
                            goToDashboard()
                        else
                            Text(text = stringResource(R.string.login_failure))

                    Button(
                        modifier = Modifier
                            .widthIn(min = 200.dp)
                            .height(dimensionResource(R.dimen.standard_height_button)), onClick = {
                            keyboardController?.hide()
                            viewModel.checkLogin(
                                user = userTextState.value.text,
                                password = passwordTextState.value.text
                            )
                        }, enabled = loginEnabled
                    ) {
                        Text(text = stringResource(R.string.login))
                    }

                    Spacer(modifier.height(dimensionResource(R.dimen.standard_height)))

                    TextButton(
                        modifier = Modifier
                            .widthIn(min = 200.dp)
                            .height(dimensionResource(R.dimen.standard_height_button)),
                        onClick = { goToTerms() }

                    ) {
                        Text(text = stringResource(R.string.terms_and_conditions))
                    }
                }

                if (state.value.loading)
                    MeteoMartoCircularProgressIndicator()

            }

        }

    }

}

@Preview
@Composable
fun LoginPreview() {

    LoginScreen(goToTerms = {}, goToDashboard = {})

}

