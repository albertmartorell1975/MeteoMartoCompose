package com.martorell.albert.meteomartocompose.ui.screens.dashboard

import android.content.Intent
import android.location.Location
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.martorell.albert.meteomartocompose.utils.LocationManagerCustom
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CityWeatherScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    var showSettingsDialog by remember { mutableStateOf(false) }

    val locationManagerCustom = remember {
        LocationManagerCustom(
            context = context,
            fusedLocationProviderManager = LocationServices.getFusedLocationProviderClient(context)
        )
    }

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    var location by remember {
        mutableStateOf<Location?>(null)
    }

    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (location != null) {
            Text(
                text = "${location!!.latitude} ${location!!.longitude}",
                fontSize = 32.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        } else {

            if (showSettingsDialog) {

                AlertDialog(
                    onDismissRequest = { showSettingsDialog = false },
                    title = { Text("Location Permission Required") },
                    text = {
                        Text("Location permission has been permanently denied. Please enable it in app settings to continue.")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            context.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
                            showSettingsDialog = false
                        }) {
                            Text("Open Settings")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showSettingsDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

        }

        Spacer(Modifier.height(40.dp))

        Button(onClick = {

            if (!locationPermissions.allPermissionsGranted || locationPermissions.shouldShowRationale) {
                locationPermissions.launchMultiplePermissionRequest()
            } else {
                coroutineScope.launch @androidx.annotation.RequiresPermission(allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION]) {
                    location = locationManagerCustom.getLocation()
                    if (location == null)
                        showSettingsDialog = true
                }
            }

        }) {

            Text(text = "Get my location")
        }

    }

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