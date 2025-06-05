package com.martorell.albert.meteomartocompose.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.MeteoMartoComposeLayout
import com.martorell.albert.meteomartocompose.ui.screens.shared.CircularProgressIndicatorCustom
import com.martorell.albert.meteomartocompose.ui.screens.shared.SnackBarCustom
import com.martorell.albert.meteomartocompose.ui.screens.shared.TextFieldCustom
import kotlin.reflect.KSuspendFunction2

/**
 * To keep the LoginContent as stateless composable (so a composable that does not hold any state),
 * we apply the programming pattern well known as state hoisting, where we move the state to the caller of a composable.
 * The simple way to do it is by replacing the state with a parameter and use functions to represent events.
 * The parameter "state" is the current value to be displayed, and the event is a lambda function that gets triggered
 * whenever the state needs to be updated. Lambadas are a common approach to describe events on a composable
 */
@Composable
fun LoginScreen(
    goToTerms: () -> Unit,
    goToDashboard: () -> Unit,
    goToSignUp: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>()
) {

    val state = viewModel.state.collectAsState()

    LoginContent(
        goToTerms = { goToTerms() },
        goToDashboard = { goToDashboard() },
        goToSignUp = { goToSignUp() },
        state = state,
        onEmailChange = viewModel::setEmail,
        onPasswordChange = viewModel::setPassword,
        onPasswordVisibilityChange = viewModel::setPasswordVisible,
        loginUnchecked = viewModel::loginUnchecked,
        checkLogin = viewModel::checkLogin,
        loginEnabled = viewModel::buttonEnabled,
        logInClicked = viewModel::logInClicked
    )

}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    goToTerms: () -> Unit,
    goToDashboard: () -> Unit,
    goToSignUp: () -> Unit,
    state: State<LoginViewModel.UiState>,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    loginUnchecked: () -> Unit,
    checkLogin: () -> Unit,
    loginEnabled: () -> Boolean,
    logInClicked: KSuspendFunction2<String, String, Unit>
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    MeteoMartoComposeLayout {

        Scaffold(snackbarHost = {
            SnackbarHost(snackBarHostState)
        }) { innerPadding ->

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .imePadding()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.padding_small),
                        Alignment.CenterVertically
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = stringResource(R.string.login_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier.height(dimensionResource(R.dimen.standard_height)))
                    TextFieldCustom(
                        value = state.value.email,
                        title = R.string.label_email_text_field,
                        placeholder = R.string.placeholder_email_text_field,
                        keyboardType = KeyboardType.Email,
                        onValueChanged = {
                            onEmailChange(it)
                            loginUnchecked()
                        },
                        onTrailIconChange = { }
                    )

                    TextFieldCustom(
                        value = state.value.password,
                        title = R.string.label_password_text_field,
                        placeholder = R.string.placeholder_password_text_field,
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password,
                        onValueChanged = {
                            onPasswordChange(it)
                            loginUnchecked()
                        },
                        passwordVisible = state.value.passwordVisible,
                        onTrailIconChange = { onPasswordVisibilityChange(it) }
                    )

                    Spacer(modifier.height(dimensionResource(R.dimen.standard_height)))

                    Button(
                        modifier = Modifier
                            .widthIn(min = 200.dp)
                            .height(dimensionResource(R.dimen.standard_height_button)), onClick = {
                            keyboardController?.hide()
                            checkLogin()
                        }, enabled = loginEnabled()
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

                    TextButton(
                        modifier = Modifier
                            .widthIn(min = 200.dp)
                            .height(dimensionResource(R.dimen.standard_height_button)),
                        onClick = { goToSignUp() }

                    ) {
                        Text(text = stringResource(R.string.sign_up_call_to_action))
                    }

                    Spacer(modifier.height(dimensionResource(R.dimen.standard_height)))

                }

                if (state.value.loginChecked)
                    if (state.value.loginStatus)
                        LaunchedEffect(key1 = state.value.loginStatus) {
                            logInClicked(
                                state.value.email,
                                state.value.password
                            )
                        }
                    else {

                        snackBarHostState.SnackBarCustom(
                            title = R.string.title_login_snack_bar,
                            action = R.string.action_login_snack_bar,
                            key = arrayOf(
                                !state.value.validUser,
                                !state.value.loginStatus
                            ),
                            coroutineScope = coroutineScope,
                            performAction = {},
                            performDismissed = {}
                        )

                    }
                if (state.value.validUser)
                    goToDashboard()

                if (state.value.loading)
                    CircularProgressIndicatorCustom()

            }

        }

    }

}