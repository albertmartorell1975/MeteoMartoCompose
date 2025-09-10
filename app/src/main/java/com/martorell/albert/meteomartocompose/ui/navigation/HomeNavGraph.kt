package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.navigation.shared.ProvideAppBarAction
import com.martorell.albert.meteomartocompose.ui.navigation.shared.ProvideAppBarTitle
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherScreen
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherViewModel
import com.martorell.albert.meteomartocompose.ui.screens.favorites.FavoritesScreen

@Composable
fun HomeNavGraph(
    viewModel: CityWeatherViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    setFabVisibility: (isVisible: Boolean) -> Unit
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

                //val sharedCityWeatherViewModel: CityWeatherViewModel =
                //    if (navController.previousBackStackEntry != null) hiltViewModel(
                //        navController.previousBackStackEntry!!
                //    ) else
                //        hiltViewModel()

                ProvideAppBarTitle {
                    Text(text = stringResource(R.string.city_top_bar_title))
                }

                ProvideAppBarAction {
                    // Add whichever actions are applicable to this screen.
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
                            launchSingleTop = true
                            popUpTo<DashboardScreens.CityWeather> {
                                inclusive = true
                            }
                        }
                    },
                    setFabVisibility)
            }

            composable<DashboardScreens.Favorites> { _ ->

                /*val sharedCityWeatherViewModel: CityWeatherViewModel =
                    if (navController.previousBackStackEntry != null) hiltViewModel(
                        navController.previousBackStackEntry!!
                    ) else
                        hiltViewModel()

                 */

                ProvideAppBarTitle {
                    Text(text = stringResource(R.string.favorite_top_bar_title))
                }

                FavoritesScreen(
                    modifier = modifier.padding(),
                    goToDetail = {
                        navController.navigate(SubGraphs.FavoritesGraph(cityName = it?.name))
                    })

            }

        }

        favoriteSubGraph(navController = navController)

    }

}