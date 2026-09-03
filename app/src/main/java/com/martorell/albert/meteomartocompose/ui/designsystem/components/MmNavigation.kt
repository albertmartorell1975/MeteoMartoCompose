package com.martorell.albert.meteomartocompose.ui.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
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
    currentRoute: Any?,
    onItemClick: (Any) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
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
                        MmText.LabelSmall(text = item.label)
                    },
                )
            }
        },
    ) {
        Scaffold(
            topBar = topBar,
            floatingActionButton = floatingActionButton,
            snackbarHost = snackbarHost,
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
            currentRoute = "home",
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
