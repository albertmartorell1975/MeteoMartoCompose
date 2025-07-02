package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.martorell.albert.meteomartocompose.ui.screens.dashboard.CityWeatherScreen
import com.martorell.albert.meteomartocompose.ui.screens.dashboard.WeatherWarningsScreen

@Composable

fun HomeNavGraph(
    navController: NavHostController,
    modifier: Modifier,
  //  cityWeatherViewModel: CityWeatherViewModel
) {

    NavHost(
        navController = navController,
        startDestination = SubGraphs.Dashboard
    )
    {

        // navigation és l'inici del sub-graph de navegació
        navigation<SubGraphs.Dashboard>(
            startDestination = DashboardScreens.CityWeather,
        ) {

            composable<DashboardScreens.CityWeather> { entry ->
                //CityWeatherScreen(viewModel = cityWeatherViewModel)
                CityWeatherScreen()
            }

            composable<DashboardScreens.Favorites> { entry ->
                CityWeatherScreen()
               // CityWeatherScreen(viewModel = cityWeatherViewModel)
            }

            composable<DashboardScreens.WeatherWarnings> { entry ->
                WeatherWarningsScreen(
                    modifier = modifier.padding()
                )
            }

        }

    }

}