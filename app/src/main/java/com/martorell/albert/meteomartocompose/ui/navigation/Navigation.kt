package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.martorell.albert.meteomartocompose.ui.screens.dashboard.HomeScreen

@Composable
fun Navigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = SubGraphs.Auth
    ) {
        authSubGraph(navController = navController)
        composable<SubGraphs.Dashboard> {
            HomeScreen()
        }
    }

}