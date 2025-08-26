package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.ui.AppState
import com.martorell.albert.meteomartocompose.ui.MeteoMartoComposeLayout
import com.martorell.albert.meteomartocompose.ui.navigation.shared.TopAppBarCustom
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherViewModel
import com.martorell.albert.meteomartocompose.ui.screens.shared.AlertDialogCustom
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1
import kotlin.reflect.KSuspendFunction0

@Composable
fun FavoritesScreen(
    appState: AppState,
    sharedCityWeatherViewModel: CityWeatherViewModel,
    modifier: Modifier = Modifier,
    goToDetail: (CityWeatherDomain?) -> Unit,
    goToLogin: () -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel<FavoritesViewModel>()
) {

    val state = viewModel.state.collectAsState()

    FavoriteContent(
        modifier = modifier,
        state = state,
        goToDetail = goToDetail,
        displayAlertDialogAction = viewModel::userClickedOnDeleteFavoriteCity,
        dismissAlertDialogAction = viewModel::userDismissedAlertDialog,
        removeCityFromFavoritesAction = viewModel::removeCityFromFavorites,
        goToLoginAction = goToLogin,
        logOutAction = sharedCityWeatherViewModel::onLogOutClicked,
        appState = appState
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteContent(
    modifier: Modifier = Modifier,
    state: State<FavoritesViewModel.UiState>,
    appState: AppState,
    goToDetail: (CityWeatherDomain?) -> Unit,
    displayAlertDialogAction: KFunction1<String, Unit>,
    dismissAlertDialogAction: () -> Unit,
    removeCityFromFavoritesAction: KSuspendFunction0<Unit>,
    logOutAction: () -> Unit,
    goToLoginAction: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(scrollState)
    var openAlertDialog by rememberSaveable { mutableStateOf(false) }

    MeteoMartoComposeLayout {

        Scaffold(
            // It is the connection between the nested scroll the TopAppBar behaviour
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                if (appState.showBottomNavigation) {
                    TopAppBarCustom(
                        scrollBehavior = scrollBehavior,
                        logOut = {
                            openAlertDialog = true
                        }

                    )
                }
            }
        ) { innerPadding ->

            if (state.value.error != null) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    FavoriteEmptyState(stringResource(R.string.city_forecast_error))
                }

            } else {

                if (state.value.citiesFavorites.isEmpty()) {

                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        FavoriteEmptyState(stringResource(R.string.no_favorites_cities))
                    }

                } else {

                    if (openAlertDialog)

                        AlertDialogCustom(
                            title = R.string.logout_title,
                            content = R.string.logout_explanation,
                            actionText = R.string.logout_accept,
                            dismissText = R.string.logout_cancel,
                            onDismissAction = {
                                openAlertDialog = false
                            },
                            onConfirmAction = {
                                logOutAction()
                                openAlertDialog = false
                                goToLoginAction()
                            })

                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
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

    }

}