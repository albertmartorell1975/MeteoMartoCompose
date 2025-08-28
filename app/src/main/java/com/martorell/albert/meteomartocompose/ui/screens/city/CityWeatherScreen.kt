package com.martorell.albert.meteomartocompose.ui.screens.city

import android.Manifest
import android.content.Intent
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.screens.shared.AlertDialogCustom
import com.martorell.albert.meteomartocompose.ui.screens.shared.CircularProgressIndicatorCustom
import com.martorell.albert.meteomartocompose.ui.screens.shared.CityTextView
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

@Composable
fun CityWeatherScreen(
    viewModel: CityWeatherViewModel,
    goToLogin: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    CityWeatherContent(
        state = state,
        getLocation = viewModel::getCurrentLocationStarted,
        hideGPSDialog = viewModel::gpsDialogHid,
        showRationaleDialog = viewModel::rationaleDialogShowed,
        hideRationaleDialog = viewModel::rationaleDialogHid,
        goToLoginAction = goToLogin,
        dismissLogOutDialogAction = viewModel::hideLogOutDialog,
        logOutAction = viewModel::onLogOutClicked
    )

}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CityWeatherContent(
    state: State<CityWeatherViewModel.UiState>,
    getLocation: KSuspendFunction0<Unit>,
    hideGPSDialog: () -> Unit,
    showRationaleDialog: () -> Unit,
    hideRationaleDialog: () -> Unit,
    dismissLogOutDialogAction: () -> Unit,
    logOutAction: () -> Unit,
    goToLoginAction: () -> Unit
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (state.value.logOut)
            AlertDialogCustom(
                title = R.string.logout_title,
                content = R.string.logout_explanation,
                actionText = R.string.logout_accept,
                dismissText = R.string.logout_cancel,
                onDismissAction = { dismissLogOutDialogAction() },
                onConfirmAction = {
                    logOutAction()
                    dismissLogOutDialogAction()
                    goToLoginAction()
                })

        if (state.value.locationChecked && state.value.showRationale) {

            AlertDialogCustom(
                title = R.string.location_rationale_title,
                content = R.string.location_rationale_explanation,
                actionText = R.string.location_rationale_action,
                dismissText = R.string.location_rationale_cancel,
                onDismissAction = hideRationaleDialog,
                onConfirmAction = {
                    context.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
                    hideRationaleDialog()
                })
        }

        if (state.value.locationChecked && !state.value.permissionsGranted) {

            if (!locationPermissions.shouldShowRationale) {
                LaunchedEffect(key1 = state.value.permissionsGranted) {
                    locationPermissions.launchMultiplePermissionRequest()
                }
            } else showRationaleDialog()

        } else {

            if (state.value.locationChecked && state.value.showGPSDialog) {

                AlertDialogCustom(
                    title = R.string.location_request_title,
                    content = R.string.location_request_explanation,
                    actionText = R.string.location_request_action,
                    dismissText = R.string.location_request_cancel,
                    onDismissAction = hideGPSDialog,
                    onConfirmAction = {
                        context.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
                        hideGPSDialog()
                    })

            }
        }

        if (state.value.loadedForecast) {

            if (state.value.errorLocation != null || state.value.errorForecast != null) {

                CityTextView(
                    contentFix = stringResource(R.string.city_forecast_error),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

            } else {

                state.value.coordinates.fold({}) {

                    state.value.city?.name?.let { cityName ->

                        CityTextView(
                            contentFix = cityName,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(dimensionResource(R.dimen.medium_spacer)))
                    }

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(state.value.city?.weatherIcon).crossfade(true).build(),
                        contentDescription = "El temps que fa",
                        modifier = Modifier
                            .height(dimensionResource(R.dimen.weather_icon_size))
                            .width(
                                dimensionResource(R.dimen.weather_icon_size)
                            ),
                        contentScale = ContentScale.Crop
                    )

                    state.value.city?.weatherDescription?.let { weatherDescription ->

                        CityTextView(
                            contentFix = weatherDescription,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    CityTextView(
                        contentFix = stringResource(R.string.city_current_temperature),
                        contentDynamic = state.value.city?.temperature.toString()
                    )

                    CityTextView(
                        contentFix = stringResource(R.string.city_max_temperature),
                        contentDynamic = state.value.city?.temperatureMax.toString(),
                        colorDynamic = Color.Red
                    )

                    CityTextView(
                        contentFix = stringResource(R.string.city_min_temperature),
                        contentDynamic = state.value.city?.temperatureMin.toString(),
                        colorDynamic = Color.Blue
                    )

                    CityTextView(
                        contentFix = stringResource(R.string.city_pressure),
                        contentDynamic = state.value.city?.pressure.toString()
                    )

                    CityTextView(
                        contentFix = stringResource(R.string.city_rain),
                        contentDynamic = state.value.city?.rain.toString(),
                        colorDynamic = Color.Blue
                    )

                }

            }

        }

        Button(onClick = {

            coroutineScope.launch {
                getLocation()
            }

        }) {

            Text(text = stringResource(R.string.update_forecast))

        }

    }

    if (state.value.loading) CircularProgressIndicatorCustom()

}

@Preview(showBackground = true)
@Composable
fun CityForecastData() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .padding(
                paddingValues = PaddingValues(
                    start = 24.dp,
                    end = 24.dp
                )
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Sabadell 333",
            fontSize = 35.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(dimensionResource(R.dimen.standard_height)))

        AsyncImage(
            model = if (LocalInspectionMode.current) {
                "https://example.com/image.jpg"
            } else {
                Icons.Default.Place
            },
            contentDescription = "El temps que fa",
            modifier = Modifier
                .height(dimensionResource(R.dimen.weather_icon_size))
                .width(
                    dimensionResource(R.dimen.weather_icon_size)
                ),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(dimensionResource(R.dimen.medium_spacer)))

        Text(
            text = "Temps assolejat",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(dimensionResource(R.dimen.medium_spacer)))

        Text(
            text = "Temperatura actual",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.W400
        )

        Spacer(Modifier.height(dimensionResource(R.dimen.medium_spacer)))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Temperatura Màxima",
            fontSize = 25.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.W400
        )

        Spacer(Modifier.height(dimensionResource(R.dimen.medium_spacer)))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Temperatura Mínima",
            fontSize = 25.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.W400
        )

        Spacer(Modifier.height(dimensionResource(R.dimen.medium_spacer)))

        CityTextView(contentFix = "Pressió atomsfèrica")

    }

}
