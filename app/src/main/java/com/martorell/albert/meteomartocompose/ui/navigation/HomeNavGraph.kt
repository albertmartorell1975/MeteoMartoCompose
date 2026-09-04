package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.martorell.albert.meteomartocompose.ui.AppState
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherScreen
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherViewModel
import com.martorell.albert.meteomartocompose.ui.screens.city.HighTemperatureAlertScreen
import com.martorell.albert.meteomartocompose.ui.screens.favorites.FavoritesScreen

fun NavGraphBuilder.dashboardGraph(
    appState: AppState,
    nestedScrollConnection: NestedScrollConnection,
    modifier: Modifier = Modifier,
    setFabVisibility: (isVisible: Boolean) -> Unit,
) {
    navigation<SubGraphs.Dashboard>(
        startDestination = DashboardScreens.CityWeather
    ) {

        composable<DashboardScreens.CityWeather> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                appState.getBackStackEntry(SubGraphs.Dashboard)
            }
            val viewModel: CityWeatherViewModel = hiltViewModel(parentEntry)

            CityWeatherScreen(
                modifier = modifier,
                viewModel = viewModel,
                nestedScrollConnection = nestedScrollConnection,
                goToLogin = {
                    appState.navigate(SubGraphs.Auth) {
                        popUpTo(SubGraphs.Dashboard) {
                            inclusive = true
                        }
                    }
                },
                goToHighTempAlert = { temperature ->
                    appState.navigate(DashboardScreens.HighTemperatureAlert(temperature))
                },
                setFabVisibility = setFabVisibility
            )
        }

        composable<DashboardScreens.Favorites> {
            FavoritesScreen(
                modifier = modifier.padding(),
                nestedScrollConnection = nestedScrollConnection,
                goToDetail = { city ->
                    appState.navigate(SubGraphs.FavoritesGraph(cityName = city?.name))
                }
            )
        }

        dialog<DashboardScreens.HighTemperatureAlert>(
            deepLinks = listOf(
                navDeepLink<DashboardScreens.HighTemperatureAlert>(
                    basePath = "meteomarto://alert/high-temperature"
                )
            ),
            dialogProperties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<DashboardScreens.HighTemperatureAlert>()
            HighTemperatureAlertScreen(
                temperature = route.temperature,
                onDismiss = {
                    appState.navigateUp()
                }
            )
        }
    }
}
