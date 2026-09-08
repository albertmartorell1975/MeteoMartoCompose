package com.martorell.albert.meteomartocompose.ui.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "w400dp-h800dp-xhdpi")
class LoginScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_initialState() {
        composeTestRule.setContent {
            val state = remember { mutableStateOf(LoginViewModel.UiState()) }
            MeteoMartoTheme {
                LoginContent(
                    goToTerms = {},
                    goToSignUp = {},
                    state = state,
                    onEmailChange = {},
                    onPasswordChange = {},
                    onPasswordVisibilityChange = {},
                    loginUnchecked = {},
                    onEmailBlur = {},
                    onEmailFocus = {},
                    onPasswordBlur = {},
                    onPasswordFocus = {},
                    checkLogin = {},
                    loginEnabled = { false }
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/login_screen_initial.png")
    }

    @Test
    fun loginScreen_loadingState() {
        composeTestRule.setContent {
            val state = remember { mutableStateOf(LoginViewModel.UiState(loading = true)) }
            MeteoMartoTheme {
                LoginContent(
                    goToTerms = {},
                    goToSignUp = {},
                    state = state,
                    onEmailChange = {},
                    onPasswordChange = {},
                    onPasswordVisibilityChange = {},
                    loginUnchecked = {},
                    onEmailBlur = {},
                    onEmailFocus = {},
                    onPasswordBlur = {},
                    onPasswordFocus = {},
                    checkLogin = {},
                    loginEnabled = { false }
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/login_screen_loading.png")
    }
}
