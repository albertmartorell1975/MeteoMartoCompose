package com.martorell.albert.meteomartocompose.ui.navigation.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmDevicePreview
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPreview
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmText
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme

/** Stateless top app bar displaying a single-line title with optional back navigation and logout actions. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarCustom(
    title: String,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    showLogoutAction: Boolean = false,
    onLogoutClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        title = {
            MmText.TitleLarge(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        modifier = modifier,
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                    )
                }
            }
        },
        actions = {
            if (showLogoutAction) {
                IconButton(onClick = onLogoutClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = stringResource(R.string.logout_title),
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = MeteoMartoTheme.colors.surfaceContainer,
            navigationIconContentColor = MeteoMartoTheme.colors.onSurface,
            titleContentColor = MeteoMartoTheme.colors.onSurface,
            actionIconContentColor = MeteoMartoTheme.colors.onSurface,
        ),
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@MmPreview
@MmDevicePreview
@Composable
private fun TopAppBarCustomTopLevelPreview() {
    MeteoMartoTheme {
        Surface(color = MeteoMartoTheme.colors.background) {
            TopAppBarCustom(
                title = "Meteo Marto",
                showLogoutAction = true,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@MmPreview
@MmDevicePreview
@Composable
private fun TopAppBarCustomDetailPreview() {
    MeteoMartoTheme {
        Surface(color = MeteoMartoTheme.colors.background) {
            TopAppBarCustom(
                title = "City weather",
                showBackButton = true,
            )
        }
    }
}
