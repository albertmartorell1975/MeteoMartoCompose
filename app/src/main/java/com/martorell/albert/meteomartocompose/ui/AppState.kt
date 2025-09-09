package com.martorell.albert.meteomartocompose.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.navigation.DashboardScreens
import com.martorell.albert.meteomartocompose.ui.navigation.navigatePoppingUpToStartDestination
import com.martorell.albert.meteomartocompose.ui.navigation.shared.TopLevelRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): AppState = remember(
    navController,
    key2 = coroutineScope
) {
    AppState(
        navController = navController,
        coroutineScope = coroutineScope
    )
}

class AppState(
    private val navController: NavHostController,
    private val coroutineScope: CoroutineScope
) {

    companion object {

        val BOTTOM_NAV_OPTIONS = listOf(
            TopLevelRoute(
                R.string.city_tab,
                DashboardScreens.CityWeather,
                Icons.Default.LocationCity
            ),
            TopLevelRoute(
                R.string.favorite_tab,
                DashboardScreens.Favorites,
                Icons.Default.Favorite
            )
        )

    }

    val showFavoriteButton: Boolean
        @Composable get() {

            BOTTOM_NAV_OPTIONS.forEach { _ ->

                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry.value?.destination ?: return false
                currentDestination.route?.let {
                    return it.contains(stringResource(id = R.string.city_tab))
                }

            }

            return false

        }

    val showBottomNavigation: Boolean
        @Composable get() {

            BOTTOM_NAV_OPTIONS.forEach { destination ->

                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry.value?.destination ?: return true

                if (currentDestination.hierarchy.any {
                        it.hasRoute(destination.route::class)
                    } == true)
                    return true

            }

            return false

        }


    // This state variable directly controls whether the FAB should be part of the composition. It's initialized to false (hidden by default).
    var isFabVisible by mutableStateOf(false)
        private set

    fun updateIsFabVisible(newValue: Boolean) {
        isFabVisible = newValue
    }

    var userClickedOnFab by mutableIntStateOf(0)
        private set

    fun updateOnFabClick() {
        userClickedOnFab =
            userClickedOnFab + 1 // This will now trigger recomposition for composables reading it
    }

    @Composable
    fun selectCurrentDestination(screen: DashboardScreens): Boolean {

        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentDestination = navBackStackEntry?.destination
        return currentDestination?.hierarchy?.any { it.hasRoute(screen::class) } == true

    }

    @Composable
    fun fabIcon(isCityFavoriteAction: suspend () -> Boolean): ImageVector? {

        // According to if the just loaded city is favorite or not, we display the proper icon
        val icon by produceState<ImageVector?>(
            initialValue = null,
            key1 = userClickedOnFab
        ) {

            value = if (isCityFavoriteAction.invoke()) {
                Icons.Default.Favorite
            } else
                Icons.Default.FavoriteBorder
        }

        return icon

    }

    // actions
    fun onNavItemClick(screen: DashboardScreens) {

        navController.navigatePoppingUpToStartDestination(screen)

    }

    fun onFabClick(onFabAction: () -> Unit) {

        coroutineScope.launch { onFabAction.invoke() }
        updateOnFabClick()

    }

}