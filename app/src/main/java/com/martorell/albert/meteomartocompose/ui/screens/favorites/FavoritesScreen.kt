package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmDialog
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmErrorState
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmLoadingOverlay
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPreview
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmSecondaryButton
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmText
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmTertiaryButton
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme
import com.martorell.albert.meteomartocompose.ui.mappers.toMessage

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    nestedScrollConnection: NestedScrollConnection? = null,
    goToDetail: (CityWeatherDomain?) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel<FavoritesViewModel>()
) {

    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onStart()
    }

    FavoriteContent(
        modifier = modifier.then(
            if (nestedScrollConnection != null) Modifier.nestedScroll(nestedScrollConnection) else Modifier
        ),
        state = state,
        goToDetail = goToDetail,
        onRetry = viewModel::onStart,
        displayAlertDialogAction = viewModel::userClickedOnDeleteFavoriteCity,
        dismissAlertDialogAction = viewModel::userDismissedAlertDialog,
        removeCityFromFavoritesAction = viewModel::removeCityFromFavorites
    )

}

@Composable
fun FavoriteContent(
    modifier: Modifier = Modifier,
    state: State<FavoritesViewModel.UiState>,
    goToDetail: (CityWeatherDomain?) -> Unit,
    onRetry: () -> Unit,
    displayAlertDialogAction: (String) -> Unit,
    dismissAlertDialogAction: () -> Unit,
    removeCityFromFavoritesAction: () -> Unit
) {

    Box(modifier = modifier.fillMaxSize()) {
        when (val content = state.value.content) {
            is FavoritesViewModel.FavoritesContent.Loading -> {
                MmLoadingOverlay()
            }

            is FavoritesViewModel.FavoritesContent.Error -> {
                MmErrorState(
                    message = content.error.toMessage(),
                    onRetry = onRetry
                )
            }

            is FavoritesViewModel.FavoritesContent.Success -> {
                if (content.cities.isEmpty()) {
                    FavoriteEmptyState(stringResource(R.string.no_favorites_cities))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(MeteoMartoTheme.spacing.small),
                        verticalArrangement = Arrangement.spacedBy(MeteoMartoTheme.spacing.extraSmall)
                    ) {
                        items(count = content.cities.size) { index ->
                            FavoriteItem(
                                city = content.cities[index],
                                clickOnDelete = {
                                    displayAlertDialogAction(content.cities[index].name)
                                },
                                clickOnRow = { goToDetail(content.cities[index]) }
                            )
                        }
                    }
                }
            }
        }

        if (state.value.cityToUnMarkAsFavorite.isNotEmpty()) {
            MmDialog(
                onDismissRequest = dismissAlertDialogAction,
                title = { MmText.HeadlineMedium(stringResource(R.string.delete_favority_city_title)) },
                text = { MmText.BodyMedium(stringResource(R.string.delete_favority_city_explanation)) },
                confirmButton = {
                    MmSecondaryButton(onClick = removeCityFromFavoritesAction) {
                        MmText.BodyLarge(stringResource(R.string.delete_favority_city_action))
                    }
                },
                dismissButton = {
                    MmTertiaryButton(onClick = dismissAlertDialogAction) {
                        MmText.BodyLarge(stringResource(R.string.delete_favority_city_cancel))
                    }
                }
            )
        }
    }
}

@MmPreview
@Composable
private fun FavoritesScreenPreview() {
    val dummyState = androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(
            FavoritesViewModel.UiState(
                content = FavoritesViewModel.FavoritesContent.Success(
                    cities = listOf(
                        CityWeatherDomain(name = "Sabadell", temperature = 25.0, temperatureMin = 20.0, temperatureMax = 30.0, pressure = 1012),
                        CityWeatherDomain(name = "Barcelona", temperature = 28.0, temperatureMin = 22.0, temperatureMax = 32.0, pressure = 1010)
                    )
                )
            )
        )
    }

    MeteoMartoTheme {
        FavoriteContent(
            state = dummyState,
            goToDetail = {},
            onRetry = {},
            displayAlertDialogAction = {},
            dismissAlertDialogAction = {},
            removeCityFromFavoritesAction = { }
        )
    }
}
