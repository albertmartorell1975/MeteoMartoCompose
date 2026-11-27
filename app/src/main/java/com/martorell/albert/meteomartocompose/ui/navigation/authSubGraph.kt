package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.martorell.albert.meteomartocompose.ui.screens.auth.LoginScreen
import com.martorell.albert.meteomartocompose.ui.screens.auth.SignUpScreen
import com.martorell.albert.meteomartocompose.ui.screens.auth.TermsScreen

fun NavGraphBuilder.authSubGraph(
    appState: AppState,
) {
    navigation<SubGraphs.Auth>(
        startDestination = AuthScreens.Login,
    ) {
        composable<AuthScreens.Login> {
            LoginScreen(
                snackbarHostState = appState.snackbarHostState,
                goToTerms = appState::navigateToTerms,
                goToDashboard = appState::navigateToDashboard,
                goToSignUp = appState::navigateToSignUp,
            )
        }

        composable<AuthScreens.SignUp> {
            SignUpScreen(
                snackbarHostState = appState.snackbarHostState,
                goToDashboard = appState::navigateToDashboard,
                goToLogin = appState::navigateUp,
            )
        }

        composable<AuthScreens.Terms> {
            TermsScreen(
                goToLogin = appState::navigateUp,
            )
        }
    }
}
