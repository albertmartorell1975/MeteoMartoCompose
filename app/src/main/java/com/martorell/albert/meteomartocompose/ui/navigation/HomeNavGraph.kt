package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.foundation.layout.padding
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
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = DashboardScreens.CityWeather
    )
    {

        composable<DashboardScreens.CityWeather> { entry ->
            CityWeatherScreen()
        }

        composable<DashboardScreens.Favorites> { entry ->

            FavoritesScreen(
                modifier = modifier.padding(),
                goToDetail = {
                    navController.navigate(SubGraphs.Favorites)
                }
            )

            // No eliminar. És un exemple de com compartir ViewModel
            /*val sharedViewModel: CityWeatherViewModel =
                if (navController.previousBackStackEntry != null) hiltViewModel(
                    navController.previousBackStackEntry!!
                ) else
                    hiltViewModel()

            FavoritesScreen(
                modifier = modifier.padding(),
                onTestSharedViewModel = sharedViewModel::isCityFavorite,
                goToDetail = {
                    navController.navigate(SubGraphs.Favorites)
                }
            )

             */
        }

        favoriteSubGraph(navController = navController)

    }

}