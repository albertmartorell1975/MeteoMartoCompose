package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherScreen
import com.martorell.albert.meteomartocompose.ui.screens.favorites.FavoritesScreen

@Composable

fun HomeNavGraph(
    navController: NavHostController,
    modifier: Modifier
    //  cityWeatherViewModel: CityWeatherViewModel
) {

    NavHost(
        navController = navController,
        startDestination = DashboardScreens.CityWeather
    )
    {

        composable<DashboardScreens.CityWeather> { entry ->
            //CityWeatherScreen(viewModel = cityWeatherViewModel)
            CityWeatherScreen()
        }

        composable<DashboardScreens.Favorites> { entry ->
            FavoritesScreen(onClick = {
                navController.navigate(SubGraphs.Favorites)
            })
        }

        favoriteSubGraph(navController = navController)

    }

}