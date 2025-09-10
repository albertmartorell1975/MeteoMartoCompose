package com.martorell.albert.meteomartocompose.ui.screens.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.AppState
import com.martorell.albert.meteomartocompose.ui.MeteoMartoComposeLayout
import com.martorell.albert.meteomartocompose.ui.navigation.HomeNavGraph
import com.martorell.albert.meteomartocompose.ui.navigation.shared.NavigationBarCustom
import com.martorell.albert.meteomartocompose.ui.navigation.shared.TopAppBarCustom
import com.martorell.albert.meteomartocompose.ui.rememberAppState
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    appState: AppState = rememberAppState(navController = navController),
    cityWeatherViewModel: CityWeatherViewModel = hiltViewModel(),
) {

    val scrollState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(scrollState)

    MeteoMartoComposeLayout {

        Scaffold(
            // It is the connection between the nested scroll the TopAppBar behaviour
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBarCustom(
                    navController = navController,
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                if (appState.showBottomNavigation) {
                    NavigationBarCustom(
                        appState = appState
                    )
                }
            },
            floatingActionButton = {

                if (appState.showFavoriteButton && appState.isFabVisible) {

                    FloatingActionButton(onClick = {
                        appState.onFabClick { cityWeatherViewModel::onFavoriteClicked.invoke() }
                    }) {

                        val icon =
                            appState.fabIcon(isCityFavoriteAction = { cityWeatherViewModel::isCityFavorite.invoke() })
                        icon?.run {
                            Icon(
                                imageVector = icon,
                                contentDescription = stringResource(R.string.favorite)
                            )
                        }
                    }

                }
            }

        ) { innerPadding ->

            // Scaffold's content
            // We pass a lambda setFabVisibility to it. This lambda takes a Boolean argument.
            HomeNavGraph(
                viewModel = cityWeatherViewModel,
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                setFabVisibility = { newVisibility ->
                    appState.updateIsFabVisible(newVisibility)
                })

        }
    }

}