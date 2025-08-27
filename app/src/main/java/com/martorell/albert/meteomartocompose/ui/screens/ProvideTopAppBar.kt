package com.martorell.albert.meteomartocompose.ui.screens

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.LocalOwnersProvider

@Composable
fun ProvideAppBarAction(actions: @Composable RowScope.() -> Unit) {
    if (LocalViewModelStoreOwner.current == null || LocalViewModelStoreOwner.current !is NavBackStackEntry)
        return
    val actionViewModel = viewModel(initializer = { TopAppBarViewModel() })
    SideEffect {
        actionViewModel.actionState = actions
    }
}

@Composable
fun ProvideAppBarTitle(title: @Composable () -> Unit) {
    if (LocalViewModelStoreOwner.current == null || LocalViewModelStoreOwner.current !is NavBackStackEntry)
        return
    val actionViewModel = viewModel(initializer = { TopAppBarViewModel() })
    SideEffect {
        actionViewModel.titleState = title
    }
}


@Composable
fun ProvideAppBarNavigationIcon(navigationIcon: @Composable () -> Unit) {

    if (LocalViewModelStoreOwner.current == null || LocalViewModelStoreOwner.current !is NavBackStackEntry)
        return
    val actionViewModel = viewModel(initializer = { TopAppBarViewModel() })
    SideEffect {
        actionViewModel.navigationState = navigationIcon
    }

}

@Composable
fun RowScope.AppBarAction(navBackStackEntry: NavBackStackEntry?) {
    val stateHolder = rememberSaveableStateHolder()
    navBackStackEntry?.LocalOwnersProvider(stateHolder) {
        val actionViewModel = viewModel(initializer = { TopAppBarViewModel() })
        actionViewModel.actionState?.let { it() }
    }
}


@Composable
fun AppBarTitle(navBackStackEntry: NavBackStackEntry?) {
    val stateHolder = rememberSaveableStateHolder()
    navBackStackEntry?.LocalOwnersProvider(stateHolder) {
        val actionViewModel = viewModel(initializer = { TopAppBarViewModel() })
        actionViewModel.titleState?.let { it() }
    }
}

@Composable
fun AppBarIcon(navBackStackEntry: NavBackStackEntry?) {
    val stateHolder = rememberSaveableStateHolder()
    navBackStackEntry?.LocalOwnersProvider(stateHolder) {
        val actionViewModel = viewModel(initializer = { TopAppBarViewModel() })
        actionViewModel.navigationState?.let { it() }
    }
}

private class TopAppBarViewModel : ViewModel() {

    var titleState by mutableStateOf(null as (@Composable () -> Unit)?, referentialEqualityPolicy())
    var actionState by mutableStateOf(
        null as (@Composable RowScope.() -> Unit)?,
        referentialEqualityPolicy()
    )
    var navigationState by mutableStateOf(null as (@Composable () -> Unit)?, referentialEqualityPolicy())

}