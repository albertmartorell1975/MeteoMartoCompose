package com.martorell.albert.meteomartocompose.ui.screens.city

import android.content.Intent
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmPreview
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.LocalFndSpacing
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.MeteoMartoTheme
import com.martorell.albert.meteomartocompose.ui.screens.shared.AlertDialogCustom
import com.martorell.albert.meteomartocompose.ui.screens.shared.CircularProgressIndicatorCustom
import com.martorell.albert.meteomartocompose.ui.screens.shared.CityTextView
import com.martorell.albert.meteomartocompose.utils.AppConstants
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CityWeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: CityWeatherViewModel,
    nestedScrollConnection: NestedScrollConnection? = null,
    goToLogin: () -> Unit,
    goToHighTempAlert: (Double) -> Unit,
    setFabVisibility: (isVisible: Boolean) -> Unit,
) {
    val uiState by viewModel.state.collectAsState()
    val context = LocalContext.current

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

    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted && uiState.locationChecked && !uiState.permissionsGranted) {
            viewModel.getCurrentLocationStarted()
        }
    }

    CityWeatherContent(
        modifier = modifier.then(
            if (nestedScrollConnection != null) Modifier.nestedScroll(nestedScrollConnection) else Modifier
        ),
        state = uiState,
        allPermissionsGranted = permissionState.allPermissionsGranted,
        locationRationale = permissionState.permissions.any {
            (it.permission == AppConstants.PERMISSION_FINE_LOCATION ||
                    it.permission == AppConstants.PERMISSION_COARSE_LOCATION) &&
                    (it.status as? PermissionStatus.Denied)?.shouldShowRationale == true
        },
        notificationRationale = permissionState.permissions.any {
            it.permission == AppConstants.PERMISSION_POST_NOTIFICATIONS &&
                    (it.status as? PermissionStatus.Denied)?.shouldShowRationale == true
        },
        onPermissionAction = {
            if (permissionState.shouldShowRationale) {
                viewModel.rationaleDialogShowed()
            } else {
                permissionState.launchMultiplePermissionRequest()
            }
        },
        onOpenSettings = {
            val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts(AppConstants.SCHEME_PACKAGE, context.packageName, null)
            }
            context.startActivity(intent)
        },
        onOpenLocationSettings = {
            context.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
        },
        actions = CityWeatherActions(
            onRefresh = { viewModel.getCurrentLocationStarted() },
            onHideGpsDialog = viewModel::gpsDialogHid,
            onHideRationale = viewModel::rationaleDialogHid,
            onLogoutConfirm = {
                viewModel.onLogOutClicked()
                viewModel.hideLogOutDialog()
                goToLogin()
            },
            onLogoutCancel = viewModel::hideLogOutDialog
        )
    )
}

@Composable
fun CityWeatherContent(
    modifier: Modifier = Modifier,
    state: CityWeatherViewModel.UiState,
    allPermissionsGranted: Boolean,
    locationRationale: Boolean,
    notificationRationale: Boolean,
    onPermissionAction: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenLocationSettings: () -> Unit,
    actions: CityWeatherActions
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.logOut) {
            AlertDialogCustom(
                title = R.string.logout_title,
                content = R.string.logout_explanation,
                actionText = R.string.logout_accept,
                dismissText = R.string.logout_cancel,
                onDismissAction = actions.onLogoutCancel,
                onConfirmAction = actions.onLogoutConfirm
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

            AlertDialogCustom(
                title = titleRes,
                content = contentRes,
                actionText = R.string.permissions_rationale_action,
                dismissText = R.string.location_rationale_cancel,
                onDismissAction = actions.onHideRationale,
                onConfirmAction = {
                    onOpenSettings()
                    actions.onHideRationale()
                }
            )
        }

        if (state.locationChecked && !allPermissionsGranted) {
            LaunchedEffect(allPermissionsGranted) {
                onPermissionAction()
            }
        } else if (state.locationChecked && state.showGPSDialog) {
            AlertDialogCustom(
                title = R.string.location_request_title,
                content = R.string.location_request_explanation,
                actionText = R.string.location_request_action,
                dismissText = R.string.location_request_cancel,
                onDismissAction = actions.onHideGpsDialog,
                onConfirmAction = {
                    onOpenLocationSettings()
                    actions.onHideGpsDialog()
                }
            )
        }

        if (state.loadedForecast) {
            if (state.errorLocation != null || state.errorForecast != null) {
                CityTextView(
                    contentFix = stringResource(R.string.city_forecast_error),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
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

        Button(onClick = {
            coroutineScope.launch { actions.onRefresh() }
        }) {
            Text(text = stringResource(R.string.update_forecast))
        }

        Spacer(Modifier.height(LocalFndSpacing.current.medium))
    }

    if (state.loading) CircularProgressIndicatorCustom()
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
        CityTextView(
            contentFix = city.name,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
        )

    Spacer(Modifier.height(LocalFndSpacing.current.medium))

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(city.weatherIcon).crossfade(true).build(),
        contentDescription = stringResource(R.string.weather_icon_description),
        modifier = Modifier
            .height(dimensionResource(R.dimen.weather_icon_size))
            .width(dimensionResource(R.dimen.weather_icon_size)),
        contentScale = ContentScale.Crop
    )

    city.weatherDescription?.let {
        CityTextView(contentFix = it, fontWeight = FontWeight.Bold)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CityTextView(
            showSpacer = false,
            contentFix = stringResource(R.string.city_current_temperature),
            contentDynamic = city.temperature.toString()
        )
        if (isHighTempAlertActive) {
            Spacer(Modifier.width(LocalFndSpacing.current.small))
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = stringResource(R.string.high_temp_alert_icon_description),
                tint = Color.Red,
                modifier = Modifier.size(LocalFndSpacing.current.large)
            )
        }
    }

    CityTextView(
        contentFix = stringResource(R.string.city_max_temperature),
        contentDynamic = city.temperatureMax.toString(),
        colorDynamic = Color.Red
    )

    CityTextView(
        contentFix = stringResource(R.string.city_min_temperature),
        contentDynamic = city.temperatureMin.toString(),
        colorDynamic = Color.Blue
    )

    CityTextView(
        contentFix = stringResource(R.string.city_pressure),
        contentDynamic = city.pressure.toString()
    )

    CityTextView(
        contentFix = stringResource(R.string.city_rain),
        contentDynamic = city.rain.toString(),
        colorDynamic = Color.Blue
    )
    }
}

@MmPreview
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
            allPermissionsGranted = true,
            locationRationale = false,
            notificationRationale = false,
            onPermissionAction = {},
            onOpenSettings = {},
            onOpenLocationSettings = {},
            actions = CityWeatherActions(
                onRefresh = {},
                onHideGpsDialog = {},
                onHideRationale = {},
                onLogoutConfirm = {},
                onLogoutCancel = {}
            )
        )
    }
}

data class CityWeatherActions(
    val onRefresh: suspend () -> Unit,
    val onHideGpsDialog: () -> Unit,
    val onHideRationale: () -> Unit,
    val onLogoutConfirm: () -> Unit,
    val onLogoutCancel: () -> Unit,
)
