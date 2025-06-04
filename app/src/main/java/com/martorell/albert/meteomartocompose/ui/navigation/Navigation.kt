package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.martorell.albert.meteomartocompose.ui.screens.dashboard.HomeScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()

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