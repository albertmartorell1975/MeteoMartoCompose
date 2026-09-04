package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.martorell.albert.meteomartocompose.ui.AppState
import com.martorell.albert.meteomartocompose.ui.screens.favorites.FavoritesDetailScreen
import com.martorell.albert.meteomartocompose.ui.screens.favorites.FavoritesScreen

fun NavGraphBuilder.favoriteSubGraph(
    appState: AppState,
    nestedScrollConnection: NestedScrollConnection
) {

    navigation<SubGraphs.FavoritesGraph>(
        startDestination = FavoritesScreens.FavoritesDetail()
    ) {

        composable<FavoritesScreens.FavoritesDetail> { _ ->

            FavoritesDetailScreen(
                onBack = { appState.navigateUp() }
            )

        }

        composable<DashboardScreens.Favorites> {
            FavoritesScreen(
                nestedScrollConnection = nestedScrollConnection,
                goToDetail = { city ->
                    appState.navigate(SubGraphs.FavoritesGraph(cityName = city?.name))
                }
            )
        }

    }

}
