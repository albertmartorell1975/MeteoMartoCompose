package com.martorell.albert.meteomartocompose.ui.navigation.shared

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.navigation.FloatingWindow
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.martorell.albert.meteomartocompose.ui.screens.AppBarAction
import com.martorell.albert.meteomartocompose.ui.screens.AppBarIcon
import com.martorell.albert.meteomartocompose.ui.screens.AppBarTitle
import kotlinx.coroutines.flow.filterNot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarCustom(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {

    val currentContentBackStackEntry by produceState(
        initialValue = null as NavBackStackEntry?,
        producer = {
            navController.currentBackStackEntryFlow
                .filterNot { it.destination is FloatingWindow }
                .collect { value = it }
        }
    )
    TopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            AppBarIcon(currentContentBackStackEntry)
        },
        title = {
            AppBarTitle(currentContentBackStackEntry)
        },
        actions = {
            AppBarAction(currentContentBackStackEntry)
        }
    )

}