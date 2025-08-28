package com.martorell.albert.meteomartocompose.ui.navigation.shared

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.FloatingWindow
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.LocalOwnersProvider
import kotlinx.coroutines.flow.filterNot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarCustom(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {

    // Here TopAppBarCustom consumes the composable components from appbar using viewModel
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



/**
 * Below we define the composable components of TopAppBarCustom
 * First we need to get our instance of the ViewModel.
 * We can do this by getting the current NavBackStackEntry.
 * This can be achieved using the The CompositionLocal containing the current LocalViewModelStoreOwner.
 * When accessed within a NavHost, this should be a NavBackStackEntry. If it exists, it can then be used to get the TopAppBarViewModel instance.
 *
 * Once we have that, we can simply update whichever part of the TopAppBarViewModel we require.
 * We will do this within a LaunchedEffect, with the key being the composable so that it is only updated once.
 */
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

    //LocalViewModelStoreOwner provides the ViewModelStoreOwner, for accessing ViewModels within the current scope.
    if (LocalViewModelStoreOwner.current == null || LocalViewModelStoreOwner.current !is NavBackStackEntry)
        return
    val actionViewModel = viewModel(initializer = { TopAppBarViewModel() })
    /**
     * SideEffects allow us to execute a snipped code ONLY when the composable recomposition finishes
     */
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

/**
 * We create a ViewModel to hold TopAppBarCustom's data.
 * This has 3 properties holding the composables for each part of the TopAppBar.
 * Note that we use the RowScope available to us for the actions part.
 *
 * To get an instance of this ViewModel, we can use the NavController that we’ve passed through.
 * We can do this as NavBackStackEntry is a LocalViewModelStoreOwner.
 * Using this, we can reliably get the same instance of our ViewModel when needed.
 *
 */
private class TopAppBarViewModel : ViewModel() {

    /**
     * Composable lambas are stored into the ViewModel that is controlled by NavBackStackEntry
     * Actions, title and navigation icon can have their own state restored thanks to NavBackStackEntry
     * Actions title and navigation icon composable are restored at the time the composable destination restored.
     *
     * Note: referentialEqualityPolicy(), considers a change if the memory reference of the old object is different from the memory reference of the new one.
     * Further info at https://blog.shreyaspatil.dev/leveraging-the-snapshot-mutation-policies-of-jetpack-compose
     */
    var titleState by mutableStateOf(null as (@Composable () -> Unit)?, referentialEqualityPolicy())
    var actionState by mutableStateOf(
        null as (@Composable RowScope.() -> Unit)?,
        referentialEqualityPolicy()
    )
    var navigationState by mutableStateOf(
        null as (@Composable () -> Unit)?,
        referentialEqualityPolicy()
    )

}