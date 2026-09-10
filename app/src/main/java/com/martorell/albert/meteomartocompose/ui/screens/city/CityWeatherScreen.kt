package com.martorell.albert.meteomartocompose.ui.screens.city

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmDevicePreview
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmDialog
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmLoadingOverlay
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPrimaryButton
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmSecondaryButton
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmTertiaryButton
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmText
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPreview
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme
import com.martorell.albert.meteomartocompose.utils.AppConstants

/**
 * Stateful entry point for the City Weather screen.
 *
 * Following the Stateless UI pattern, this composable handles:
 * - State collection from [CityWeatherViewModel] using lifecycle-aware collectors.
 * - Navigation events via [goToLogin] and [goToHighTempAlert].
 * - Initialization triggers (location and monitoring) via [LaunchedEffect].
 * - Permission state management and automatic requests.
 *
 * @param viewModel The state holder for this screen.
 * @param modifier Applied to the root layout.
 * @param nestedScrollConnection Optional connection to coordinate scrolling with parent layouts (e.g., TopAppBar).
 * @param goToLogin Callback to navigate to the authentication flow.
 * @param goToHighTempAlert Callback to show a dedicated alert for high temperatures.
 * @param setFabVisibility Callback to control the visibility of the global Floating Action Button.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CityWeatherScreen(
    viewModel: CityWeatherViewModel,
    modifier: Modifier = Modifier,
    nestedScrollConnection: NestedScrollConnection? = null,
    goToLogin: () -> Unit,
    goToHighTempAlert: (Double) -> Unit,
    setFabVisibility: (isVisible: Boolean) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCurrentLocationStarted()
        viewModel.startMonitoring()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { alert ->
            goToHighTempAlert(alert.currentTemperature)
        }
    }

    LaunchedEffect(uiState.showFab) {
        setFabVisibility(uiState.showFab)
    }

    val permissionsToRequest = remember { viewModel.getRequiredPermissions() }
    val permissionState = rememberMultiplePermissionsState(permissions = permissionsToRequest)

    // Combined permission and initial load logic
    LaunchedEffect(uiState.locationChecked, permissionState.allPermissionsGranted) {
        if (uiState.locationChecked) {
            if (permissionState.allPermissionsGranted) {
                if (!uiState.permissionsGranted) {
                    viewModel.getCurrentLocationStarted()
                }
            } else if (!uiState.showRationale && !uiState.showGPSDialog) {
                if (permissionState.shouldShowRationale) {
                    viewModel.rationaleDialogShowed()
                } else {
                    permissionState.launchMultiplePermissionRequest()
                }
            }
        }
    }

    CityWeatherContent(
        state = uiState,
        locationRationale = permissionState.permissions.any {
            ((it.permission == AppConstants.PERMISSION_FINE_LOCATION ||
                    it.permission == AppConstants.PERMISSION_COARSE_LOCATION) &&
                    (it.status as? PermissionStatus.Denied)?.shouldShowRationale == true)
        },
        notificationRationale = permissionState.permissions.any {
            (it.permission == AppConstants.PERMISSION_POST_NOTIFICATIONS &&
                    (it.status as? PermissionStatus.Denied)?.shouldShowRationale == true)
        },
        onOpenSettings = viewModel::onOpenAppSettingsClicked,
        onOpenLocationSettings = viewModel::onOpenLocationSettingsClicked,
        onRefresh = viewModel::getCurrentLocationStarted,
        onHideGpsDialog = viewModel::gpsDialogHid,
        onHideRationale = viewModel::rationaleDialogHid,
        onLogoutConfirm = {
            viewModel.onLogOutClicked()
            viewModel.hideLogOutDialog()
            goToLogin()
        },
        onLogoutCancel = viewModel::hideLogOutDialog,
        modifier = modifier
            .then(
                if (nestedScrollConnection != null) Modifier.nestedScroll(nestedScrollConnection) else Modifier,
            )
    )
}

/**
 * Stateless content of the City Weather screen.
 *
 * This component is a pure function that only depends on the provided [state] and
 * triggers events through callbacks. It is free of side-effects like [LaunchedEffect].
 *
 * @param state The current UI state to render.
 * @param locationRationale Whether to show the location permission rationale.
 * @param notificationRationale Whether to show the notification permission rationale.
 * @param onOpenSettings Callback to open the application settings.
 * @param onOpenLocationSettings Callback to open the location source settings.
 * @param onRefresh Callback to refresh the weather data.
 * @param onHideGpsDialog Callback to hide the GPS request dialog.
 * @param onHideRationale Callback to hide the permission rationale dialog.
 * @param onLogoutConfirm Callback to confirm the logout action.
 * @param onLogoutCancel Callback to cancel the logout action.
 * @param modifier Applied to the root layout of the content.
 */
@Composable
fun CityWeatherContent(
    state: CityWeatherViewModel.UiState,
    locationRationale: Boolean,
    notificationRationale: Boolean,
    onOpenSettings: () -> Unit,
    onOpenLocationSettings: () -> Unit,
    onRefresh: () -> Unit,
    onHideGpsDialog: () -> Unit,
    onHideRationale: () -> Unit,
    onLogoutConfirm: () -> Unit,
    onLogoutCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (state.logOut) {
            MmDialog(
                onDismissRequest = onLogoutCancel,
                title = { MmText.HeadlineMedium(stringResource(R.string.logout_title)) },
                text = { MmText.BodyMedium(stringResource(R.string.logout_explanation)) },
                confirmButton = {
                    MmSecondaryButton(onClick = onLogoutConfirm) {
                        MmText.BodyLarge(stringResource(R.string.logout_accept))
                    }
                },
                dismissButton = {
                    MmTertiaryButton(onClick = onLogoutCancel) {
                        MmText.BodyLarge(stringResource(R.string.logout_cancel))
                    }
                }
            )
        }

        if (state.locationChecked && state.showRationale) {
            val titleRes = when {
                locationRationale && notificationRationale -> R.string.generic_rationale_title
                locationRationale -> R.string.location_rationale_title
                notificationRationale -> R.string.notification_rationale_title
                else -> R.string.generic_rationale_title
            }
            val contentRes = when {
                locationRationale && notificationRationale -> R.string.generic_rationale_explanation
                locationRationale -> R.string.location_rationale_explanation
                notificationRationale -> R.string.notification_rationale_explanation
                else -> R.string.generic_rationale_explanation
            }

            MmDialog(
                onDismissRequest = onHideRationale,
                title = { MmText.HeadlineMedium(stringResource(titleRes)) },
                text = { MmText.BodyMedium(stringResource(contentRes)) },
                confirmButton = {
                    MmSecondaryButton(onClick = {
                        onOpenSettings()
                        onHideRationale()
                    }) {
                        MmText.BodyLarge(stringResource(R.string.permissions_rationale_action))
                    }
                },
                dismissButton = {
                    MmTertiaryButton(onClick = onHideRationale) {
                        MmText.BodyLarge(stringResource(R.string.location_rationale_cancel))
                    }
                }
            )
        }

        if (state.locationChecked && state.showGPSDialog) {
            MmDialog(
                onDismissRequest = onHideGpsDialog,
                title = { MmText.HeadlineMedium(stringResource(R.string.location_request_title)) },
                text = { MmText.BodyMedium(stringResource(R.string.location_request_explanation)) },
                confirmButton = {
                    MmSecondaryButton(onClick = {
                        onOpenLocationSettings()
                        onHideGpsDialog()
                    }) {
                        MmText.BodyLarge(stringResource(R.string.location_request_action))
                    }
                },
                dismissButton = {
                    MmTertiaryButton(onClick = onHideGpsDialog) {
                        MmText.BodyLarge(stringResource(R.string.location_request_cancel))
                    }
                }
            )
        }

        if (state.loadedForecast) {
            if (state.errorLocation != null || state.errorForecast != null) {
                MmText.HeadlineMedium(
                    text = stringResource(R.string.city_forecast_error),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(MeteoMartoTheme.spacing.medium)
                )
            } else {
                state.city?.let { city ->
                    WeatherInfo(
                        city = city,
                        isHighTempAlertActive = state.isHighTempAlertActive,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        MmPrimaryButton(
            onClick = onRefresh,
            modifier = Modifier.padding(MeteoMartoTheme.spacing.medium)
        ) {
            MmText.BodyLarge(text = stringResource(R.string.update_forecast))
        }

        Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))
    }

    if (state.loading) MmLoadingOverlay()
}

@Composable
private fun WeatherInfo(
    city: CityWeatherDomain,
    isHighTempAlertActive: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MmText.DisplayLarge(
            text = city.name,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(city.weatherIcon).crossfade(enable = true).build(),
            contentDescription = stringResource(R.string.weather_icon_description),
            modifier = Modifier.size(dimensionResource(R.dimen.weather_icon_size)),
            contentScale = ContentScale.Crop
        )

        city.weatherDescription?.let {
            MmText.HeadlineMedium(
                text = it,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(MeteoMartoTheme.spacing.small))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MmText.BodyLarge(
                text = stringResource(R.string.city_current_temperature, city.temperature.toString()),
                textAlign = TextAlign.Center
            )
            if (isHighTempAlertActive) {
                Spacer(Modifier.width(MeteoMartoTheme.spacing.small))
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = stringResource(R.string.high_temp_alert_icon_description),
                    tint = Color.Red,
                    modifier = Modifier.size(MeteoMartoTheme.spacing.large)
                )
            }
        }

        MmText.BodyMedium(
            text = stringResource(R.string.city_max_temperature, city.temperatureMax.toString()),
            color = Color.Red,
            textAlign = TextAlign.Center
        )

        MmText.BodyMedium(
            text = stringResource(R.string.city_min_temperature, city.temperatureMin.toString()),
            color = Color.Blue,
            textAlign = TextAlign.Center
        )

        MmText.BodyMedium(
            text = stringResource(R.string.city_pressure, city.pressure.toString()),
            textAlign = TextAlign.Center
        )

        MmText.BodyMedium(
            text = stringResource(R.string.city_rain, city.rain.toString()),
            color = Color.Blue,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(MeteoMartoTheme.spacing.medium))
    }
}

@MmPreview
@MmDevicePreview
@Composable
private fun CityWeatherScreenPreview() {
    val dummyState = CityWeatherViewModel.UiState(
        loadedForecast = true,
        city = CityWeatherDomain(
            name = "Sabadell",
            temperature = 30.5,
            temperatureMax = 32.0,
            temperatureMin = 22.0,
            weatherDescription = "Sunny",
            pressure = 1012,
            rain = 0.0
        ),
        isHighTempAlertActive = true
    )

    MeteoMartoTheme {
        CityWeatherContent(
            state = dummyState,
            locationRationale = false,
            notificationRationale = false,
            onOpenSettings = {},
            onOpenLocationSettings = {},
            onRefresh = {},
            onHideGpsDialog = {},
            onHideRationale = {},
            onLogoutConfirm = {},
            onLogoutCancel = {}
        )
    }
}
