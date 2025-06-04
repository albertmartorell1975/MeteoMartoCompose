package com.martorell.albert.meteomartocompose.ui.navigation.shared

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.martorell.albert.meteomartocompose.ui.AppState

@Composable
fun NavigationBarCustom(appState: AppState) {

    NavigationBar {

        AppState.BOTTOM_NAV_OPTIONS.forEach { screen ->
            NavigationBarItem(
                selected = appState.selectCurrentDestination(screen.route) == true,
                onClick = { appState.onNavItemClick(screen.route) },
                label = { Text(text = stringResource(id = screen.title)) },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = stringResource(id = screen.title)
                    )
                })

        }
    }

}
