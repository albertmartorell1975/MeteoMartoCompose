package com.martorell.albert.meteomartocompose.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.MeteoMartoComposeLayout
import com.martorell.albert.meteomartocompose.ui.screens.shared.CircularProgressIndicatorCustom
import com.martorell.albert.meteomartocompose.ui.screens.shared.ErrorScreen
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction2

@Composable
fun SignUpScreen(
    goToDashboard: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsState()

    SignUpContent(
        signUpClick = viewModel::signUpClicked,
        goToDashboard = { goToDashboard() },
        state = state,
        onEmailChange = viewModel::setUser,
        onPasswordChange = viewModel::setPassword,
        onPasswordVisibilityChange = viewModel::setPasswordVisible,
        signUpUnchecked = viewModel::signUpUnchecked,
        signUpEnabled = viewModel::buttonEnabled,
        tryAgainClicked = viewModel::tryAgainClicked
    )

}

@Composable
fun SignUpContent(
    modifier: Modifier = Modifier,
    goToDashboard: () -> Unit,
    signUpClick: KSuspendFunction2<String, String, Unit>,
    state: State<SignUpViewModel.UiState>,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    signUpUnchecked: () -> Unit,
    signUpEnabled: () -> Boolean,
    tryAgainClicked: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    MeteoMartoComposeLayout {

        Scaffold { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center,
            ) {

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.padding_small),
                        Alignment.CenterVertically
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (!state.value.validUser && state.value.signUpChecked)
                        state.value.error?.let {
                            ErrorScreen(
                                it,
                                tryAgainClicked,
                                tryAgainClicked
                            )
                        }
                    else {
                        Text(
                            text = stringResource(R.string.sign_up_title),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier.height(dimensionResource(R.dimen.standard_height)))
                        TextField(
                            value = state.value.email,
                            onValueChange = {
                                onEmailChange(it)
                                signUpUnchecked()
                            },
                            label = { Text(text = stringResource(R.string.label_email_text_field)) },
                            placeholder = { Text(text = stringResource(R.string.placeholder_email_text_field)) },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Email
                            ),
                            isError = !state.value.signUpStatus && state.value.signUpChecked
                        )

                        TextField(
                            value = state.value.password,
                            onValueChange = {
                                onPasswordChange(it)
                                signUpUnchecked()
                            },
                            label = { Text(text = stringResource(R.string.label_password_text_field)) },
                            placeholder = { Text(text = stringResource(R.string.placeholder_password_text_field)) },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Password
                            ),
                            visualTransformation =
                                if (state.value.passwordVisible)
                                    VisualTransformation.None
                                else
                                    PasswordVisualTransformation(),
                            trailingIcon = {
                                IconToggleButton(
                                    checked = state.value.passwordVisible,
                                    onCheckedChange = { onPasswordVisibilityChange(it) }
                                ) {
                                    Icon(
                                        imageVector =
                                            if (state.value.passwordVisible)
                                                Icons.Default.VisibilityOff
                                            else
                                                Icons.Default.Visibility,
                                        contentDescription = stringResource(R.string.visibility_password)
                                    )
                                }
                            },
                            isError = !state.value.signUpStatus && state.value.signUpChecked
                        )

                        Spacer(modifier.height(dimensionResource(R.dimen.standard_height)))

                        if (state.value.validUser)
                            goToDashboard()
                        else
                            if (state.value.signUpChecked)
                                state.value.error?.let {
                                    ErrorScreen(
                                        it,
                                        tryAgainClicked,
                                        tryAgainClicked
                                    )
                                }

                        Button(
                            modifier = Modifier
                                .widthIn(min = 200.dp)
                                .height(dimensionResource(R.dimen.standard_height_button)),
                            onClick = {
                                keyboardController?.hide()
                                coroutineScope.launch {
                                    signUpClick(
                                        state.value.email,
                                        state.value.password
                                    )
                                }
                            }, enabled = signUpEnabled()
                        ) {
                            Text(text = stringResource(R.string.sign_up))
                        }

                    }
                }

                if (state.value.loading)
                    CircularProgressIndicatorCustom()

            }

        }

    }

}

@Preview
@Composable
fun SignUpPreview() {

    SignUpScreen(goToDashboard = {})

}
