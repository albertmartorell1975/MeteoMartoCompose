package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import com.martorell.albert.meteomartocompose.data.CustomErrorFlow
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
class FavoritesScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val dummyCities = listOf(
        CityWeatherDomain(name = "Barcelona", favorite = true, temperature = 25.0, temperatureMin = 20.0, temperatureMax = 30.0, pressure = 1013),
        CityWeatherDomain(name = "Sabadell", favorite = true, temperature = 22.0, temperatureMin = 18.0, temperatureMax = 26.0, pressure = 1012)
    )

    @Test
    fun favoritesScreen_successState() {
        composeTestRule.setContent {
            val state = mutableStateOf(
                FavoritesViewModel.UiState(
                    content = FavoritesViewModel.FavoritesContent.Success(dummyCities)
                )
            )
            MeteoMartoTheme {
                FavoriteContent(
                    state = state,
                    goToDetail = {},
                    onRetry = {},
                    displayAlertDialogAction = {},
                    dismissAlertDialogAction = {},
                    removeCityFromFavoritesAction = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/favorites_success.png")
    }

    @Test
    fun favoritesScreen_emptyState() {
        composeTestRule.setContent {
            val state = mutableStateOf(
                FavoritesViewModel.UiState(
                    content = FavoritesViewModel.FavoritesContent.Success(emptyList())
                )
            )
            MeteoMartoTheme {
                FavoriteContent(
                    state = state,
                    goToDetail = {},
                    onRetry = {},
                    displayAlertDialogAction = {},
                    dismissAlertDialogAction = {},
                    removeCityFromFavoritesAction = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/favorites_empty.png")
    }

    @Test
    fun favoritesScreen_errorState() {
        composeTestRule.setContent {
            val state = mutableStateOf(
                FavoritesViewModel.UiState(
                    content = FavoritesViewModel.FavoritesContent.Error(CustomErrorFlow.Connectivity)
                )
            )
            MeteoMartoTheme {
                FavoriteContent(
                    state = state,
                    goToDetail = {},
                    onRetry = {},
                    displayAlertDialogAction = {},
                    dismissAlertDialogAction = {},
                    removeCityFromFavoritesAction = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/favorites_error.png")
    }

    @Test
    fun favoritesScreen_deleteDialogState() {
        composeTestRule.setContent {
            val state = mutableStateOf(
                FavoritesViewModel.UiState(
                    content = FavoritesViewModel.FavoritesContent.Success(dummyCities),
                    cityToUnMarkAsFavorite = "Barcelona"
                )
            )
            MeteoMartoTheme {
                FavoriteContent(
                    state = state,
                    goToDetail = {},
                    onRetry = {},
                    displayAlertDialogAction = {},
                    dismissAlertDialogAction = {},
                    removeCityFromFavoritesAction = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage("src/test/snapshots/favorites_delete_dialog.png")
    }
}
