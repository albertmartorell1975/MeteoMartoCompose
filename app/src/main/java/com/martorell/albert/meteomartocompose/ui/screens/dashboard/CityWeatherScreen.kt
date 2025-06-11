package com.martorell.albert.meteomartocompose.ui.screens.dashboard

import android.content.Intent
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.screens.shared.CircularProgressIndicatorCustom
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

@Composable
fun CityWeatherScreen(viewModel: CityWeatherViewModel = hiltViewModel()) {

    val state = viewModel.state.collectAsState()
    CityWeatherContent(
        state = state,
        getLocation = viewModel::getCurrentLocationStarted,
        hideGPSDialog = viewModel::gpsDialogHid,
        showRationaleDialog = viewModel::rationaleDialogShowed,
        hideRationaleDialog = viewModel::rationaleDialogHid,

    )

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CityWeatherContent(
    state: State<CityWeatherViewModel.UiState>,
    getLocation: KSuspendFunction0<Unit>,
    hideGPSDialog: () -> Unit,
    showRationaleDialog: () -> Unit,
    hideRationaleDialog: () -> Unit
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (state.value.locationChecked && state.value.showRationale) {
            AlertDialog(
                onDismissRequest = { hideRationaleDialog() },
                title = { Text("Permisos de localització") },
                text = {
                    Text("Per obtenir la informació meteorològica, has de donar permisos de localització")
                },
                confirmButton = {
                    TextButton(onClick = {
                        context.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
                        hideRationaleDialog()
                    }) {
                        Text(stringResource(R.string.location_request_action))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { hideGPSDialog() }) {
                        Text(stringResource(R.string.location_request_cancel))
                    }
                }
            )
        }

        if (state.value.locationChecked && !state.value.permissionsGranted) {

            if (!locationPermissions.shouldShowRationale) {
                LaunchedEffect(key1 = state.value.permissionsGranted) {
                    locationPermissions.launchMultiplePermissionRequest()
                }
            } else {

                showRationaleDialog()

            }

        } else {

            if (state.value.locationChecked && !state.value.gpsEnabled) {

                AlertDialog(
                    onDismissRequest = { hideGPSDialog() },
                    title = { Text(stringResource(R.string.location_request_title)) },
                    text = {
                        Text(stringResource(R.string.location_request_explanation))
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            context.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
                            hideGPSDialog()
                        }) {
                            Text(stringResource(R.string.location_request_action))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { hideGPSDialog() }) {
                            Text(stringResource(R.string.location_request_cancel))
                        }
                    }
                )

            }
        }

        if (state.value.locationChecked && state.value.error == null) {

            state.value.coordinates.fold(

                { {} }) {
                Text(
                    text = "${it.latitude} ${it.longitude}",
                    fontSize = 32.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(40.dp))
            }

        }

        Button(onClick = {

            coroutineScope.launch {
                getLocation()
            }

        }) {

            Text(text = "Get my location")
        }

    }

    if (state.value.loading)
        CircularProgressIndicatorCustom()

}

@Preview
@Composable
fun CityWeatherScreenPreview() {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(onClick = {}) {
            Text(text = "Get my location")
        }
        Spacer(Modifier.height(40.dp))
        Text(
            text = "Hola",
            fontSize = 32.sp,
            color = Color.Red,
            fontWeight = FontWeight.Bold
        )
    }

}