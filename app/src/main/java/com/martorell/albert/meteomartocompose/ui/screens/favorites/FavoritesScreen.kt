package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPreview
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.LocalFndSpacing
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme
import com.martorell.albert.meteomartocompose.ui.screens.shared.AlertDialogCustom
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    nestedScrollConnection: NestedScrollConnection? = null,
    goToDetail: (CityWeatherDomain?) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel<FavoritesViewModel>()
) {

    val state = viewModel.state.collectAsState()

    FavoriteContent(
        modifier = modifier.then(
            if (nestedScrollConnection != null) Modifier.nestedScroll(nestedScrollConnection) else Modifier
        ),
        state = state,
        goToDetail = goToDetail,
        displayAlertDialogAction = viewModel::userClickedOnDeleteFavoriteCity,
        dismissAlertDialogAction = viewModel::userDismissedAlertDialog,
        removeCityFromFavoritesAction = viewModel::removeCityFromFavorites
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteContent(
    modifier: Modifier = Modifier,
    state: State<FavoritesViewModel.UiState>,
    goToDetail: (CityWeatherDomain?) -> Unit,
    displayAlertDialogAction: (String) -> Unit,
    dismissAlertDialogAction: () -> Unit,
    removeCityFromFavoritesAction: suspend () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    if (state.value.error != null) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            FavoriteEmptyState(stringResource(R.string.city_forecast_error))
        }

    } else {

        if (state.value.citiesFavorites.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                FavoriteEmptyState(stringResource(R.string.no_favorites_cities))
            }

        } else {

            LazyColumn(
                modifier = modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(LocalFndSpacing.current.small),
                verticalArrangement = Arrangement.spacedBy(LocalFndSpacing.current.extraSmall)
            ) {

                items(count = state.value.citiesFavorites.size) { index ->
                    FavoriteItem(
                        city = state.value.citiesFavorites[index],
                        clickOnDelete = {
                            displayAlertDialogAction(state.value.citiesFavorites[index].name)
                        },
                        clickOnRow = { goToDetail(state.value.citiesFavorites[index]) }
                    )
                }
            }

            if (state.value.cityToUnMarkAsFavorite.isNotEmpty())
                AlertDialogCustom(
                    title = R.string.delete_favority_city_title,
                    content = R.string.delete_favority_city_explanation,
                    actionText = R.string.delete_favority_city_action,
                    dismissText = R.string.delete_favority_city_cancel,
                    onDismissAction = dismissAlertDialogAction,
                    onConfirmAction = {
                        coroutineScope.launch {
                            removeCityFromFavoritesAction()
                        }
                    })

        }

    }

}

@MmPreview
@Composable
private fun FavoritesScreenPreview() {
    val dummyState = androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(
            FavoritesViewModel.UiState(
                citiesFavorites = listOf(
                    CityWeatherDomain(name = "Sabadell", temperature = 25.0, temperatureMin = 20.0, temperatureMax = 30.0, pressure = 1012),
                    CityWeatherDomain(name = "Barcelona", temperature = 28.0, temperatureMin = 22.0, temperatureMax = 32.0, pressure = 1010)
                )
            )
        )
    }

    MeteoMartoTheme {
        FavoriteContent(
            state = dummyState,
            goToDetail = {},
            displayAlertDialogAction = {},
            dismissAlertDialogAction = {},
            removeCityFromFavoritesAction = { }
        )
    }
}
