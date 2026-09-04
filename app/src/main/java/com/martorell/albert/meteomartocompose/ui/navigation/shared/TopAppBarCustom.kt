package com.martorell.albert.meteomartocompose.ui.navigation.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.AppState
import com.martorell.albert.meteomartocompose.ui.navigation.DashboardScreens
import com.martorell.albert.meteomartocompose.ui.navigation.SubGraphs
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarCustom(
    appState: AppState,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val destination = appState.currentDestination
    val titleRes = appState.topAppBarTitle
    val shouldShowBackButton = appState.shouldShowBackButton

    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (shouldShowBackButton) {
                IconButton(onClick = { appState.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        title = {
            titleRes?.let {
                Text(text = stringResource(it))
            }
        },
        actions = {
            if (destination?.hasRoute(DashboardScreens.CityWeather::class) == true) {
                val dashboardEntry = remember(destination) {
                    appState.getBackStackEntry(SubGraphs.Dashboard)
                }
                val viewModel: CityWeatherViewModel = hiltViewModel(dashboardEntry)
                IconButton(onClick = { viewModel.showLogOutDialog() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Logout,
                        contentDescription = stringResource(R.string.logout_title)
                    )
                }
            }
        }
    )
}
