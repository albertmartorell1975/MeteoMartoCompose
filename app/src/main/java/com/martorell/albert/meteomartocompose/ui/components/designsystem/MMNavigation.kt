package com.martorell.albert.meteomartocompose.ui.components.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.martorell.albert.meteomartocompose.ui.theme.MeteoMartoTheme

/**
 * Data class representing a single destination in the [MMNavigation].
 *
 * @param route The navigation route associated with this item.
 * @param icon The icon to display for this item.
 * @param label The text label to display.
 * @param contentDescription Accessibility description for the icon.
 */
data class MMNavigationItem(
    val route: Any,
    val icon: ImageVector,
    val label: String,
    val contentDescription: String? = null,
)

/**
 * [MMNavigation] is an adaptive scaffold that automatically switches between
 * a Bottom Navigation Bar (smartphones), a Navigation Rail (tablet), or a Navigation Drawer (foldable)
 * based on the device's window size class (Phone, Tablet, Foldable).
 *
 * This component centralizes adaptive navigation patterns for the entire application.
 *
 * @param items List of [MMNavigationItem] to be displayed in the navigation area.
 * @param currentRoute The currently active route to highlight the selected item.
 * @param onItemClick Callback triggered when a navigation item is selected.
 * @param modifier Optional [Modifier] for the scaffold.
 * @param topBar Optional top bar to be displayed.
 * @param floatingActionButton Optional floating action button.
 * @param content The main UI content to be displayed within the scaffold.
 */
@Composable
fun MMNavigation(
    items: List<MMNavigationItem>,
    currentRoute: Any?,
    onItemClick: (Any) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    NavigationSuiteScaffold(
        modifier = modifier,
        navigationSuiteItems = {
            items.forEach { item ->
                item(
                    selected = currentRoute == item.route,
                    onClick = { onItemClick(item.route) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.contentDescription ?: item.label,
                        )
                    },
                    label = {
                        MMText.LabelSmall(text = item.label)
                    },
                )
            }
        },
    ) {
        Scaffold(
            topBar = topBar,
            floatingActionButton = floatingActionButton,
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                content()
            }
        }
    }
}

// --- Previews ---

@MMDevicePreview
@Composable
private fun MMNavigationPreview() {
    val sampleItems = listOf(
        MMNavigationItem(
            route = "home",
            icon = Icons.Default.Home,
            label = "Home",
        ),
        MMNavigationItem(
            route = "favorites",
            icon = Icons.Default.Favorite,
            label = "Favorites",
        ),
        MMNavigationItem(
            route = "settings",
            icon = Icons.Default.Settings,
            label = "Settings",
        ),
    )

    MeteoMartoTheme {
        MMNavigation(
            items = sampleItems,
            currentRoute = "home",
            onItemClick = {},
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                MMText.DisplayLarge(
                    text = "Adaptive Content",
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
