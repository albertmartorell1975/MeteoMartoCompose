package com.martorell.albert.meteomartocompose.ui.screens.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.martorell.albert.meteomartocompose.ui.AppState
import com.martorell.albert.meteomartocompose.ui.MeteoMartoComposeLayout
import com.martorell.albert.meteomartocompose.ui.navigation.HomeNavGraph
import com.martorell.albert.meteomartocompose.ui.navigation.shared.NavigationBarCustom
import com.martorell.albert.meteomartocompose.ui.navigation.shared.TopAppBarCustom
import com.martorell.albert.meteomartocompose.ui.rememberAppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    appState: AppState = rememberAppState(navController = navController),
    viewModel: CityWeatherViewModel = hiltViewModel()
) {

    val scrollState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(scrollState)

    MeteoMartoComposeLayout {

        Scaffold(
            // It is the connection between the nested scroll the TopAppBar behaviour
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                if (appState.showBottomNavigation) {
                    TopAppBarCustom(
                        scrollBehavior = scrollBehavior,
                        markCityAsFavorite = viewModel::onFavoriteClicked
                    )
                }
            },
            bottomBar = {
                if (appState.showBottomNavigation) {
                    NavigationBarCustom(
                        appState = appState
                    )
                }

            }

        ) { innerPadding ->

            // Scaffold's content
            HomeNavGraph(
                navController = navController,
                Modifier.padding(innerPadding)
            )

        }
    }

}