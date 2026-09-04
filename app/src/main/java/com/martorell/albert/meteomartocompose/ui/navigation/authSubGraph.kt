package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.martorell.albert.meteomartocompose.ui.AppState
import com.martorell.albert.meteomartocompose.ui.screens.auth.LoginScreen
import com.martorell.albert.meteomartocompose.ui.screens.auth.SignUpScreen
import com.martorell.albert.meteomartocompose.ui.screens.auth.TermsScreen

fun NavGraphBuilder.authSubGraph(
    appState: AppState,
) {

    navigation<SubGraphs.Auth>(
        startDestination = AuthScreens.Login
    ) {

        composable<AuthScreens.Login> {
            LoginScreen(
                snackbarHostState = appState.snackbarHostState,
                goToTerms = { appState.navigate(AuthScreens.Terms) },
                goToDashboard = {
                    appState.navigate(SubGraphs.Dashboard) {
                        popUpTo(SubGraphs.Auth) { inclusive = true }
                    }
                },
                goToSignUp = { appState.navigate(AuthScreens.SignUp) }
            )
        }

        composable<AuthScreens.SignUp> {
            SignUpScreen(
                goToDashboard = {
                    appState.navigate(SubGraphs.Dashboard) {
                        popUpTo(SubGraphs.Auth) { inclusive = true }
                    }
                }
            )
        }

        composable<AuthScreens.Terms> {
            TermsScreen(
                goToLogin = { appState.navigateUp() }
            )
        }

    }

}
