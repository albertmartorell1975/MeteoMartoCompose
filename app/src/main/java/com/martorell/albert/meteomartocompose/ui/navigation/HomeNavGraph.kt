package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.martorell.albert.meteomartocompose.ui.AppState
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherScreen
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherViewModel
import com.martorell.albert.meteomartocompose.ui.screens.favorites.FavoritesScreen

@Composable
fun HomeNavGraph(
    appState: AppState,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = SubGraphs.Dashboard
    ) {

        navigation<SubGraphs.Dashboard>(
            startDestination = DashboardScreens.CityWeather
        ) {

            authSubGraph(
                navController = navController,
                logOut = true
            )

            composable<DashboardScreens.CityWeather> { _ ->
                CityWeatherScreen(
                    appState = appState,
                    goToLogin = {
                        navController.navigate(SubGraphs.Auth) {
                            launchSingleTop = true
                            popUpTo<DashboardScreens.CityWeather> {
                                inclusive = true
                            }
                        }
                    })
            }

            composable<DashboardScreens.Favorites> { _ ->

                val sharedCityWeatherViewModel: CityWeatherViewModel =
                    if (navController.previousBackStackEntry != null) hiltViewModel(
                        navController.previousBackStackEntry!!
                    ) else
                        hiltViewModel()

                FavoritesScreen(
                    appState = appState,
                    modifier = modifier.padding(),
                    sharedCityWeatherViewModel = sharedCityWeatherViewModel,
                    goToDetail = {
                        navController.navigate(SubGraphs.FavoritesGraph(cityName = it?.name))
                    },
                    goToLogin = {
                        navController.navigate(SubGraphs.Auth) {
                            launchSingleTop = true
                            popUpTo<DashboardScreens.CityWeather> {
                                inclusive = true
                            }
                        }
                    })

            }

        }

        favoriteSubGraph(navController = navController)

    }

}