package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.navigation.shared.ProvideAppBarAction
import com.martorell.albert.meteomartocompose.ui.navigation.shared.ProvideAppBarTitle
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherScreen
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherViewModel
import com.martorell.albert.meteomartocompose.ui.screens.city.HighTemperatureAlertScreen
import com.martorell.albert.meteomartocompose.ui.screens.favorites.FavoritesScreen

fun NavGraphBuilder.dashboardGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    setFabVisibility: (isVisible: Boolean) -> Unit
) {
    navigation<SubGraphs.Dashboard>(
        startDestination = DashboardScreens.CityWeather
    ) {

        composable<DashboardScreens.CityWeather> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(SubGraphs.Dashboard)
            }
            val viewModel: CityWeatherViewModel = hiltViewModel(parentEntry)

            ProvideAppBarTitle {
                Text(text = stringResource(R.string.city_top_bar_title))
            }

            ProvideAppBarAction {
                IconButton(
                    onClick = {
                        viewModel.showLogOutDialog()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Logout,
                        contentDescription = stringResource(R.string.logout_title)
                    )
                }
            }

            CityWeatherScreen(
                modifier = modifier,
                viewModel = viewModel,
                goToLogin = {
                    navController.navigate(SubGraphs.Auth) {
                        popUpTo(SubGraphs.Dashboard) {
                            inclusive = true
                        }
                    }
                },
                goToHighTempAlert = { temperature ->
                    navController.navigate(DashboardScreens.HighTemperatureAlert(temperature))
                },
                setFabVisibility = setFabVisibility
            )
        }

        composable<DashboardScreens.Favorites> {
            ProvideAppBarTitle {
                Text(text = stringResource(R.string.favorite_top_bar_title))
            }

            FavoritesScreen(
                modifier = modifier.padding(),
                goToDetail = {
                    navController.navigate(SubGraphs.FavoritesGraph(cityName = it?.name))
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
                    navController.popBackStack()
                }
            )
        }
    }
}
