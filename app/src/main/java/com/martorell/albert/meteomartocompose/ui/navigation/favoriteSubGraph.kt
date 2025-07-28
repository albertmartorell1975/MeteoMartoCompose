package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.martorell.albert.meteomartocompose.ui.screens.favorites.FavoritesDetailScreen

fun NavGraphBuilder.favoriteSubGraph() {

    navigation<SubGraphs.FavoritesGraph>(
        startDestination = FavoritesScreens.FavoritesDetail()
    ) {

        composable<FavoritesScreens.FavoritesDetail> { navBackStackEntry ->

            //val args = navBackStackEntry.toRoute<FavoritesScreens.FavoritesDetail>()
            //requireNotNull(args.cityName)// des d'aquest punt assegurem que "id" no és null
            // Nota: les dues línies anteriors no fan falta posar-les ja que a FavoritesDetail s'injecta
            // el ViewModel el qual ja té  assignat el valor de cityName a través de SavedStateHandle
            FavoritesDetailScreen()
        }

    }

}