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
class SignUpScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun signUpScreen_initialState() {
        composeTestRule.setContent {
            val state = remember { mutableStateOf(SignUpViewModel.UiState()) }
            MeteoMartoTheme {
                SignUpContent(
                    goToLogin = {},
                    state = state,
                    onEmailChange = {},
                    onPasswordChange = {},
                    onPasswordVisibilityChange = {},
                    signUpUnchecked = {},
                    onEmailBlur = {},
                    onEmailFocus = {},
                    onPasswordBlur = {},
                    onPasswordFocus = {},
                    checkSignUp = {},
                    signUpEnabled = { false },
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/signup_screen_initial.png")
    }

    @Test
    fun signUpScreen_loadingState() {
        composeTestRule.setContent {
            val state = remember { mutableStateOf(SignUpViewModel.UiState(loading = true)) }
            MeteoMartoTheme {
                SignUpContent(
                    goToLogin = {},
                    state = state,
                    onEmailChange = {},
                    onPasswordChange = {},
                    onPasswordVisibilityChange = {},
                    signUpUnchecked = {},
                    onEmailBlur = {},
                    onEmailFocus = {},
                    onPasswordBlur = {},
                    onPasswordFocus = {},
                    checkSignUp = {},
                    signUpEnabled = { false },
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/signup_screen_loading.png")
    }
}
