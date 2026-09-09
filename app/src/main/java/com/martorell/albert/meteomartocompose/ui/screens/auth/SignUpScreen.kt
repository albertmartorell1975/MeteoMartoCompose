package com.martorell.albert.meteomartocompose.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmDevicePreview
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmLoadingOverlay
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPrimaryButton
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPreview
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmTertiaryButton
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmText
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmTextField
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme
import com.martorell.albert.meteomartocompose.ui.mappers.asStringRes

@Composable
fun SignUpScreen(
    snackbarHostState: SnackbarHostState,
    goToDashboard: () -> Unit,
    goToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel<SignUpViewModel>(),
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()

    LaunchedEffect(state.value.validUser) {
        if (state.value.validUser) {
            goToDashboard()
        }
    }

    val signUpFailureAction = stringResource(R.string.action_login_snack_bar)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SignUpViewModel.SignUpEvent.SignUpError -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(event.error.asStringRes()),
                        actionLabel = signUpFailureAction,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    SignUpContent(
        modifier = modifier,
        goToLogin = goToLogin,
        state = state,
        onEmailChange = {
            snackbarHostState.currentSnackbarData?.dismiss()
            viewModel.setEmail(it)
        },
        onPasswordChange = {
            snackbarHostState.currentSnackbarData?.dismiss()
            viewModel.setPassword(it)
        },
        onPasswordVisibilityChange = viewModel::setPasswordVisible,
        signUpUnchecked = viewModel::signUpUnchecked,
        onEmailBlur = viewModel::validateEmail,
        onEmailFocus = viewModel::clearEmailError,
        onPasswordBlur = viewModel::validatePassword,
        onPasswordFocus = viewModel::clearPasswordError,
        checkSignUp = viewModel::performSignUp,
        signUpEnabled = viewModel::buttonEnabled,
    )
}

@Composable
fun SignUpContent(
    modifier: Modifier = Modifier,
    goToLogin: () -> Unit,
    state: State<SignUpViewModel.UiState>,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    signUpUnchecked: () -> Unit,
    onEmailBlur: () -> Unit,
    onEmailFocus: () -> Unit,
    onPasswordBlur: () -> Unit,
    onPasswordFocus: () -> Unit,
    checkSignUp: () -> Unit,
    signUpEnabled: () -> Boolean,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MeteoMartoTheme.spacing.medium)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(
                MeteoMartoTheme.spacing.small,
                Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MmText.HeadlineMedium(
                text = stringResource(R.string.sign_up_title),
                color = MeteoMartoTheme.colors.primary
            )

            Spacer(Modifier.height(MeteoMartoTheme.spacing.large))

            MmTextField(
                modifier = Modifier
                    .width(MeteoMartoTheme.dimensions.authFormWidth)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            onEmailFocus()
                        } else {
                            onEmailBlur()
                        }
                    },
                value = state.value.email,
                onValueChange = {
                    onEmailChange(it)
                    signUpUnchecked()
                },
                label = { MmText.LabelSmall(stringResource(R.string.label_email_text_field)) },
                placeholder = { MmText.BodyMedium(stringResource(R.string.placeholder_email_text_field)) },
                isError = !state.value.isEmailValid,
                supportingText = if (!state.value.isEmailValid) {
                    { MmText.LabelSmall(stringResource(R.string.error_invalid_email)) }
                } else null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
            )

            MmTextField(
                modifier = Modifier
                    .width(MeteoMartoTheme.dimensions.authFormWidth)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            onPasswordFocus()
                        } else {
                            onPasswordBlur()
                        }
                    },
                value = state.value.password,
                onValueChange = {
                    onPasswordChange(it)
                    signUpUnchecked()
                },
                label = { MmText.LabelSmall(stringResource(R.string.label_password_text_field)) },
                placeholder = { MmText.BodyMedium(stringResource(R.string.placeholder_password_text_field)) },
                isError = !state.value.isPasswordValid,
                supportingText = if (!state.value.isPasswordValid) {
                    { MmText.LabelSmall(stringResource(R.string.error_invalid_password)) }
                } else null,
                visualTransformation = if (state.value.passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconToggleButton(
                        checked = state.value.passwordVisible,
                        onCheckedChange = { onPasswordVisibilityChange(it) }
                    ) {
                        Icon(
                            imageVector = if (state.value.passwordVisible) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = stringResource(R.string.visibility_password)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        checkSignUp()
                    }
                ),
            )

            Spacer(Modifier.height(MeteoMartoTheme.spacing.large))

            MmPrimaryButton(
                modifier = Modifier
                    .width(MeteoMartoTheme.dimensions.authFormWidth)
                    .height(MeteoMartoTheme.dimensions.buttonHeight),
                onClick = {
                    keyboardController?.hide()
                    checkSignUp()
                },
                enabled = signUpEnabled()
            ) {
                MmText.BodyLarge(stringResource(R.string.sign_up))
            }

            Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))

            MmTertiaryButton(
                modifier = Modifier
                    .widthIn(min = MeteoMartoTheme.dimensions.buttonMinWidth)
                    .height(MeteoMartoTheme.dimensions.buttonHeight),
                onClick = goToLogin
            ) {
                MmText.BodyMedium(stringResource(R.string.login_call_to_action))
            }

            Spacer(Modifier.height(MeteoMartoTheme.spacing.large))
        }

        if (state.value.loading) {
            MmLoadingOverlay()
        }
    }
}

@MmPreview
@MmDevicePreview
@Composable
private fun SignUpScreenPreview() {
    val dummyState = remember {
        mutableStateOf(
            SignUpViewModel.UiState(
                email = "newuser@example.com",
                password = "password123"
            )
        )
    }

    MeteoMartoTheme {
        SignUpContent(
            goToLogin = {},
            state = dummyState,
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordVisibilityChange = {},
            signUpUnchecked = {},
            onEmailBlur = {},
            onEmailFocus = {},
            onPasswordBlur = {},
            onPasswordFocus = {},
            checkSignUp = {},
            signUpEnabled = { true }
        )
    }
}
