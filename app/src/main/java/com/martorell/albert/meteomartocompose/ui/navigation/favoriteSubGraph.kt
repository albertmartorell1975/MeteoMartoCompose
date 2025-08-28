package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.navigation.shared.ProvideAppBarNavigationIcon
import com.martorell.albert.meteomartocompose.ui.navigation.shared.ProvideAppBarTitle
import com.martorell.albert.meteomartocompose.ui.screens.favorites.FavoritesDetailScreen

fun NavGraphBuilder.favoriteSubGraph(navController: NavHostController) {

    navigation<SubGraphs.FavoritesGraph>(
        startDestination = FavoritesScreens.FavoritesDetail()
    ) {

        composable<FavoritesScreens.FavoritesDetail> { _ ->

            ProvideAppBarTitle {
                Text(text = stringResource(R.string.city_weather_detail))
            }

            ProvideAppBarNavigationIcon {
                IconButton(
                    onClick = { navController.navigateUp() },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }

            FavoritesDetailScreen(navController = navController)

        }

    }

}