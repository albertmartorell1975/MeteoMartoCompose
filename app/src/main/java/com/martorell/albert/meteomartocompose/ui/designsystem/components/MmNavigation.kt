package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

/**
 * Immutable navigation destination metadata consumed by [MmNavigation].
 *
 * @property route Typed or string route identifier passed to `onItemClick` when selected.
 * @property icon Vector icon rendered inside the navigation bar, rail, or drawer item.
 * @property label Short localized label displayed alongside or beneath [icon].
 * @property contentDescription Optional accessibility description for [icon], defaulting to [label].
 */
@Immutable
data class MmNavigationItem(
    val route: Any,
    val icon: ImageVector,
    val label: String,
    val contentDescription: String? = null,
)

/**
 * Adaptive navigation shell built on `NavigationSuiteScaffold`, switching between a bottom
 * navigation bar on compact widths and a navigation rail or drawer on larger screens,
 * while falling back to a single `Scaffold` when [items] is empty.
 *
 * Inner content automatically receives and consumes scaffold `WindowInsets`.
 *
 * @param items Top-level navigation destinations; pass an empty list to hide the navigation chrome.
 * @param currentRoute Active `NavDestination` used by the default [isItemSelected] predicate.
 * @param onItemClick Callback invoked with the tapped [MmNavigationItem.route].
 * @param modifier Modifier applied to the outer navigation scaffold.
 * @param isItemSelected Predicate determining whether a given [MmNavigationItem] is active.
 * @param topBar Optional top app bar composable rendered above [content].
 * @param floatingActionButton Optional floating action button slot.
 * @param snackbarHost Optional snackbar host slot, such as [MmSnackbarHost].
 * @param content Main screen body rendered inside the scaffold content area.
 */
@Composable
fun MmNavigation(
    items: List<MmNavigationItem>,
    currentRoute: NavDestination?,
    onItemClick: (Any) -> Unit,
    modifier: Modifier = Modifier,
    isItemSelected: (MmNavigationItem) -> Boolean = { item ->
        currentRoute?.hierarchy?.any { it.hasRoute(item.route::class) } == true
    },
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    if (items.isEmpty()) {
        Scaffold(
            modifier = modifier,
            topBar = topBar,
            floatingActionButton = floatingActionButton,
            snackbarHost = snackbarHost,
            containerColor = Color.Transparent,
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
            ) {
                content()
            }
        }
    } else {
        val colors = MeteoMartoTheme.colors
        val navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = colors.surfaceContainer,
            navigationBarContentColor = colors.onSurface,
            navigationRailContainerColor = colors.surfaceContainer,
            navigationRailContentColor = colors.onSurface,
            navigationDrawerContainerColor = colors.surfaceContainer,
            navigationDrawerContentColor = colors.onSurface,
        )
        val navigationItemColors = NavigationSuiteDefaults.itemColors(
            navigationBarItemColors = NavigationBarItemDefaults.colors(
                selectedIconColor = colors.onSecondaryContainer,
                selectedTextColor = colors.onSurface,
                indicatorColor = colors.secondaryContainer,
                unselectedIconColor = colors.onSurfaceVariant,
                unselectedTextColor = colors.onSurfaceVariant,
            ),
            navigationRailItemColors = NavigationRailItemDefaults.colors(
                selectedIconColor = colors.onSecondaryContainer,
                selectedTextColor = colors.onSurface,
                indicatorColor = colors.secondaryContainer,
                unselectedIconColor = colors.onSurfaceVariant,
                unselectedTextColor = colors.onSurfaceVariant,
            ),
            navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = colors.secondaryContainer,
                unselectedContainerColor = Color.Transparent,
                selectedIconColor = colors.onSecondaryContainer,
                unselectedIconColor = colors.onSurfaceVariant,
                selectedTextColor = colors.onSecondaryContainer,
                unselectedTextColor = colors.onSurfaceVariant,
            ),
        )

        NavigationSuiteScaffold(
            modifier = modifier,
            navigationSuiteItems = {
                items.forEach { item ->
                    item(
                        selected = isItemSelected(item),
                        onClick = { onItemClick(item.route) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.contentDescription ?: item.label,
                            )
                        },
                        label = {
                            MmText.LabelSmall(
                                text = item.label,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        colors = navigationItemColors,
                    )
                }
            },
            navigationSuiteColors = navigationSuiteColors,
            containerColor = colors.background,
            contentColor = colors.onSurface,
        ) {
            Scaffold(
                topBar = topBar,
                floatingActionButton = floatingActionButton,
                snackbarHost = snackbarHost,
                containerColor = Color.Transparent,
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding),
                ) {
                    content()
                }
            }
        }
    }
}

@MmPreview
@MmDevicePreview
@Composable
private fun MmNavigationPreview() {
    val sampleItems = listOf(
        MmNavigationItem(
            route = "city",
            icon = Icons.Default.Home,
            label = "City",
        ),
        MmNavigationItem(
            route = "favorites",
            icon = Icons.Default.Favorite,
            label = "Favorites",
        ),
    )

    MeteoMartoTheme {
        MmNavigation(
            items = sampleItems,
            currentRoute = null,
            onItemClick = {},
            modifier = Modifier.fillMaxSize(),
            isItemSelected = { it.route == "city" },
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                MmText.HeadlineMedium(
                    text = "Adaptive Content",
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
