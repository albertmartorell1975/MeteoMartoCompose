package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import com.martorell.albert.meteomartocompose.data.CustomError
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
class FavoritesDetailScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val dummyCity = CityWeatherDomain(
        name = "Barcelona",
        temperature = 22.5,
        temperatureMin = 18.0,
        temperatureMax = 28.0,
        weatherDescription = "Partly Cloudy",
        pressure = 1015,
        rain = 0.0
    )

    @Test
    fun favoritesDetailScreen_successState() {
        composeTestRule.setContent {
            val state = mutableStateOf(
                FavoritesDetailViewModel.UiState(
                    content = FavoritesDetailViewModel.DetailContent.Success(dummyCity)
                )
            )
            MeteoMartoTheme {
                FavoritesDetailContent(
                    state = state,
                    onRetry = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/favorites_detail_success.png")
    }

    @Test
    fun favoritesDetailScreen_loadingState() {
        composeTestRule.setContent {
            val state = mutableStateOf(
                FavoritesDetailViewModel.UiState(
                    content = FavoritesDetailViewModel.DetailContent.Loading
                )
            )
            MeteoMartoTheme {
                FavoritesDetailContent(
                    state = state,
                    onRetry = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/favorites_detail_loading.png")
    }

    @Test
    fun favoritesDetailScreen_errorState() {
        composeTestRule.setContent {
            val state = mutableStateOf(
                FavoritesDetailViewModel.UiState(
                    content = FavoritesDetailViewModel.DetailContent.Error(CustomError.Connectivity)
                )
            )
            MeteoMartoTheme {
                FavoritesDetailContent(
                    state = state,
                    onRetry = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/favorites_detail_error.png")
    }
}
