package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.martorell.albert.meteomartocompose.ui.screens.favorites.FavoritesDetail

fun NavGraphBuilder.favoriteSubGraph(navController: NavHostController) {

    navigation<SubGraphs.Favorites>(startDestination = FavoritesScreens.FavoritesDetail) {

        composable<FavoritesScreens.FavoritesDetail> { entry ->
            FavoritesDetail()
        }

    }

}