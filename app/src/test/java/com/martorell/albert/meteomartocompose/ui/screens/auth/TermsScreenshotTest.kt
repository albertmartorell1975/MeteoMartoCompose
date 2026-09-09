package com.martorell.albert.meteomartocompose.ui.screens.auth

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
class TermsScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun termsScreen_initialState() {
        composeTestRule.setContent {
            MeteoMartoTheme {
                TermsContent(
                    onAcceptClick = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/terms_screen_initial.png")
    }
}
