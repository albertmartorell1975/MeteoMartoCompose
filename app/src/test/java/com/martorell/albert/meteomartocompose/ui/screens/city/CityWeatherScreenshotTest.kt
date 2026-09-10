package com.martorell.albert.meteomartocompose.ui.screens.city

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
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
class CityWeatherScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val dummyCity = CityWeatherDomain(
        name = "Barcelona",
        temperature = 22.5,
        temperatureMin = 18.0,
        temperatureMax = 28.0,
        weatherDescription = "Partly Cloudy",
        pressure = 1015,
        rain = 0.0,
        isAlertNotified = false
    )

    @Test
    fun cityWeatherScreen_successState() {
        composeTestRule.setContent {
            val state = CityWeatherViewModel.UiState(
                loadedForecast = true,
                city = dummyCity,
                isHighTempAlertActive = false
            )
            MeteoMartoTheme {
                CityWeatherContent(
                    state = state,
                    locationRationale = false,
                    notificationRationale = false,
                    onOpenSettings = {},
                    onOpenLocationSettings = {},
                    onRefresh = {},
                    onHideGpsDialog = {},
                    onHideRationale = {},
                    onLogoutConfirm = {},
                    onLogoutCancel = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/city_weather_success.png")
    }

    @Test
    fun cityWeatherScreen_highTempAlertState() {
        composeTestRule.setContent {
            val state = CityWeatherViewModel.UiState(
                loadedForecast = true,
                city = dummyCity,
                isHighTempAlertActive = true
            )
            MeteoMartoTheme {
                CityWeatherContent(
                    state = state,
                    locationRationale = false,
                    notificationRationale = false,
                    onOpenSettings = {},
                    onOpenLocationSettings = {},
                    onRefresh = {},
                    onHideGpsDialog = {},
                    onHideRationale = {},
                    onLogoutConfirm = {},
                    onLogoutCancel = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/city_weather_alert.png")
    }

    @Test
    fun cityWeatherScreen_loadingState() {
        composeTestRule.setContent {
            val state = CityWeatherViewModel.UiState(loading = true)
            MeteoMartoTheme {
                CityWeatherContent(
                    state = state,
                    locationRationale = false,
                    notificationRationale = false,
                    onOpenSettings = {},
                    onOpenLocationSettings = {},
                    onRefresh = {},
                    onHideGpsDialog = {},
                    onHideRationale = {},
                    onLogoutConfirm = {},
                    onLogoutCancel = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/city_weather_loading.png")
    }
}
