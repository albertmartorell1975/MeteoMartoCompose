package com.martorell.albert.meteomartocompose.ui.screens.city

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
class HighTemperatureAlertScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun highTemperatureAlertScreen_initialState() {
        composeTestRule.setContent {
            MeteoMartoTheme {
                HighTemperatureAlertScreen(
                    temperature = 38.5,
                    onDismiss = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/high_temp_alert_screen.png")
    }
}
