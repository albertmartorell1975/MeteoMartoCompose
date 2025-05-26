package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.martorell.albert.meteomartocompose.ui.screens.auth.LoginScreen
import com.martorell.albert.meteomartocompose.ui.screens.auth.TermsScreen

fun NavGraphBuilder.authSubGraph(navController: NavHostController) {

    navigation<SubGraphs.Auth>(startDestination = AuthScreens.Login) {

        composable<AuthScreens.Login> {

            LoginScreen(
                goToTerms = { navController.navigate(AuthScreens.Terms) },
                goToDashboard = {
                    navController.navigate(SubGraphs.Dashboard)
                    { popUpTo(SubGraphs.Auth) }
                })

        }

        composable<AuthScreens.Terms> {
            TermsScreen(goToLogin = { navController.popBackStack() })
        }

    }

}