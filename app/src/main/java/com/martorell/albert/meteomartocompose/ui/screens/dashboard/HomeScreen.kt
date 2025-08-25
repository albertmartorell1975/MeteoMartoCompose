package com.martorell.albert.meteomartocompose.ui.screens.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.AppState
import com.martorell.albert.meteomartocompose.ui.MeteoMartoComposeLayout
import com.martorell.albert.meteomartocompose.ui.navigation.DashboardScreens
import com.martorell.albert.meteomartocompose.ui.navigation.HomeNavGraph
import com.martorell.albert.meteomartocompose.ui.navigation.SubGraphs
import com.martorell.albert.meteomartocompose.ui.navigation.shared.NavigationBarCustom
import com.martorell.albert.meteomartocompose.ui.navigation.shared.TopAppBarCustom
import com.martorell.albert.meteomartocompose.ui.rememberAppState
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    appState: AppState = rememberAppState(navController = navController),
    cityWeatherViewModel: CityWeatherViewModel = hiltViewModel()
) {

    val scrollState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(scrollState)
    val coroutineScope = rememberCoroutineScope()

    MeteoMartoComposeLayout {

        Scaffold(
            // It is the connection between the nested scroll the TopAppBar behaviour
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                if (appState.showBottomNavigation) {
                    TopAppBarCustom(
                        scrollBehavior = scrollBehavior,
                        logOut = {
                            cityWeatherViewModel::onLogOutClicked.invoke()
                            navController.navigate(SubGraphs.Auth) {
                                launchSingleTop = true
                                popUpTo<DashboardScreens.CityWeather> {
                                    inclusive = true
                                }
                            }

                        }

                    )
                }
            },
            bottomBar = {
                if (appState.showBottomNavigation) {
                    NavigationBarCustom(
                        appState = appState
                    )
                }
            },
            floatingActionButton = {

                if (appState.showFavoriteButton) {

                    // According to if the just loaded city is favorite or not, we display the proper icon
                    var userClickedOnFab by remember { mutableIntStateOf(0) }

                    val icon by produceState<ImageVector?>(
                        initialValue = null,
                        key1 = userClickedOnFab
                    ) {
                        value = if (cityWeatherViewModel::isCityFavorite.invoke()) {
                            Icons.Default.Favorite
                        } else
                            Icons.Default.FavoriteBorder
                    }

                    FloatingActionButton(onClick = {
                        coroutineScope.launch { cityWeatherViewModel::onFavoriteClicked.invoke() }
                        userClickedOnFab += 1
                    }) {
                        icon?.run {
                            Icon(
                                imageVector = this,
                                contentDescription = stringResource(R.string.favorite)
                            )
                        }
                    }

                }
            }

        ) { innerPadding ->

            // Scaffold's content
            HomeNavGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )

        }
    }

}