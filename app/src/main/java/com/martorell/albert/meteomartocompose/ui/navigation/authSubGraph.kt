package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.martorell.albert.meteomartocompose.ui.screens.auth.LoginScreen
import com.martorell.albert.meteomartocompose.ui.screens.auth.SignUpScreen
import com.martorell.albert.meteomartocompose.ui.screens.auth.TermsScreen
import com.martorell.albert.meteomartocompose.ui.screens.splash.SplashScreen

fun NavGraphBuilder.authSubGraph(navController: NavHostController) {

    navigation<SubGraphs.Auth>(startDestination = AuthScreens.Splash) {

        composable<AuthScreens.Splash> {

            SplashScreen(
                goToLogin = {
                    navController.popBackStack()
                    navController.navigate(AuthScreens.Login)
                { popUpTo(SubGraphs.Auth) }},
                goToDashboard = {
                    navController.navigate(SubGraphs.Dashboard)
                    { popUpTo(SubGraphs.Auth) }
                })

        }

        composable<AuthScreens.Login> {

            LoginScreen(
                goToTerms = { navController.navigate(AuthScreens.Terms) },
                goToDashboard = {
                    navController.navigate(SubGraphs.Dashboard)
                    { popUpTo(SubGraphs.Auth) }
                },
                goToSignUp = { navController.navigate(AuthScreens.SignUp) })

        }
        composable<AuthScreens.SignUp> {
            SignUpScreen(goToDashboard = {
                navController.navigate(SubGraphs.Dashboard)
                { popUpTo(SubGraphs.Auth) }
            })
        }
        composable<AuthScreens.Terms> {
            TermsScreen(goToLogin = { navController.popBackStack() })
        }

    }

}