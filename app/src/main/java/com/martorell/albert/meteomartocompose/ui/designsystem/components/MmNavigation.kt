package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

data class MmNavigationItem(
    val route: Any,
    val icon: ImageVector,
    val label: String,
    val contentDescription: String? = null,
)

@Composable
fun MmNavigation(
    items: List<MmNavigationItem>,
    currentRoute: NavDestination?,
    onItemClick: (Any) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    if (items.isEmpty()) {
        // For screens without global navigation (Auth), use a simple transparent Scaffold.
        // We avoid NavigationSuiteScaffold here to prevent unwanted system bar artifacts.
        Scaffold(
            modifier = modifier,
            topBar = topBar,
            floatingActionButton = floatingActionButton,
            snackbarHost = snackbarHost,
            containerColor = Color.Transparent, // Let the root layout handle the background
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                content()
            }
        }
    } else {
        // For feature screens, use Adaptive Navigation.
        // NavigationSuiteScaffold automatically switches between BottomBar (mobile)
        // and NavigationRail (tablets/foldables) based on the window size.
        NavigationSuiteScaffold(
            modifier = modifier,
            navigationSuiteItems = {
                items.forEach { item ->
                    item(
                        selected = currentRoute?.hierarchy?.any { it.hasRoute(item.route::class) } == true,
                        onClick = { onItemClick(item.route) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.contentDescription ?: item.label,
                            )
                        },
                        label = {
                            MmText.LabelSmall(text = item.label)
                        },
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            // We nest a standard Scaffold inside NavigationSuiteScaffold because the latter
            // doesn't provide built-in slots for topBar, FAB, or snackbarHost.
            // This nested Scaffold manages the inner UI structure and provides correct
            // content padding (innerPadding) to avoid overlapping with bars.
            Scaffold(
                topBar = topBar,
                floatingActionButton = floatingActionButton,
                snackbarHost = snackbarHost,
                containerColor = Color.Transparent, // Transparency avoids double-background layers
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
}

@MmDevicePreview
@Composable
private fun MmNavigationPreview() {
    val sampleItems = listOf(
        MmNavigationItem(
            route = "home",
            icon = Icons.Default.Home,
            label = "Home",
        ),
    )

    MeteoMartoTheme {
        MmNavigation(
            items = sampleItems,
            currentRoute = null,
            onItemClick = {},
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                MmText.DisplayLarge(
                    text = "Adaptive Content",
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
