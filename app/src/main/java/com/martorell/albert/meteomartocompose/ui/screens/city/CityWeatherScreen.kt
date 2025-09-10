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

@Composable
fun CityWeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: CityWeatherViewModel,
    goToLogin: () -> Unit,
    setFabVisibility: (isVisible: Boolean) -> Unit
) {
    val state = viewModel.state.collectAsState()
    CityWeatherContent(
        modifier = modifier,
        state = state,
        getLocation = { viewModel.getCurrentLocationStarted() },
        hideGPSDialog = viewModel::gpsDialogHid,
        showRationaleDialog = viewModel::rationaleDialogShowed,
        hideRationaleDialog = viewModel::rationaleDialogHid,
        goToLoginAction = goToLogin,
        dismissLogOutDialogAction = viewModel::hideLogOutDialog,
        logOutAction = viewModel::onLogOutClicked,
        setFabVisibility = setFabVisibility
    )

}

/**
 * Composable function that displays the city weather information and handles location permissions.
 *
 * @param state The current UI state from the [CityWeatherViewModel].
 * @param getLocation A suspend function to trigger the location update process.
 * @param hideGPSDialog A function to hide the GPS dialog.
 * @param showRationaleDialog A function to show the location permission rationale dialog.
 * @param hideRationaleDialog A function to hide the location permission rationale dialog.
 * @param dismissLogOutDialogAction A function to dismiss the logout confirmation dialog.
 * @param logOutAction A function to perform the logout action.
 * @param goToLoginAction A function to navigate to the login screen.
 * @param setFabVisibility A function to control the visibility of the Floating Action Button
 *                         in the parent Composable (HomeScreen).
 */
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CityWeatherContent(
    modifier: Modifier = Modifier,
    state: State<CityWeatherViewModel.UiState>,
    getLocation: suspend () -> Unit,
    hideGPSDialog: () -> Unit,
    showRationaleDialog: () -> Unit,
    hideRationaleDialog: () -> Unit,
    dismissLogOutDialogAction: () -> Unit,
    logOutAction: () -> Unit,
    goToLoginAction: () -> Unit,
    setFabVisibility: (isVisible: Boolean) -> Unit
) {

    // Get the current context, UI state, and coroutine scope
    val currentContext = LocalContext.current
    val currentState = state.value
    val coroutineScope = rememberCoroutineScope()

    val locationPermissions = rememberMultiplePermissionsState(
        // Define the location permissions to request
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // setFabVisibility in CityWeatherContent is called.
        // This updates the isFabVisible state in the parent (HomeScreen).
        // Because isFabVisible is a state variable, Scaffold (and its floatingActionButton slot) will recompose.
        // LaunchedEffect to update FAB visibility when currentState.showFab changes
        LaunchedEffect(currentState.showFab) { setFabVisibility(currentState.showFab) }

        if (currentState.logOut)
        // Show logout confirmation dialog if logOut state is true
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

        if (currentState.locationChecked && currentState.showRationale) {

            // Show rationale dialog if location is checked and rationale should be shown
            AlertDialogCustom(
                title = R.string.location_rationale_title,
                content = R.string.location_rationale_explanation,
                actionText = R.string.location_rationale_action,
                dismissText = R.string.location_rationale_cancel,
                onDismissAction = hideRationaleDialog,
                onConfirmAction = {
                    // Open location settings when rationale is confirmed
                    currentContext.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
                    hideRationaleDialog()
                })
        }

        if (currentState.locationChecked && !currentState.permissionsGranted) {

            // If location is checked but permissions are not granted
            if (!locationPermissions.shouldShowRationale) {
                // If rationale should not be shown, launch permission request
                LaunchedEffect(key1 = currentState.permissionsGranted) {
                    locationPermissions.launchMultiplePermissionRequest()
                }
            } else {
                // Otherwise, show the rationale dialog
                showRationaleDialog()
            }

        } else {

            if (currentState.locationChecked && currentState.showGPSDialog) {
                // If location is checked and GPS dialog should be shown
                AlertDialogCustom(
                    title = R.string.location_request_title,
                    content = R.string.location_request_explanation,
                    actionText = R.string.location_request_action,
                    dismissText = R.string.location_request_cancel,
                    onDismissAction = hideGPSDialog,
                    // Open location settings when GPS dialog is confirmed
                    onConfirmAction = {
                        currentContext.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
                        hideGPSDialog()
                    })

            }
        }

        if (currentState.loadedForecast) {
            // If forecast data is loaded

            if (currentState.errorLocation != null || currentState.errorForecast != null) {
                // If there's an error in location or forecast

                CityTextView(
                    contentFix = stringResource(R.string.city_forecast_error),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

            } else {

                // If there are no errors, display weather information
                currentState.coordinates.fold({}) {

                    currentState.city?.name?.let { cityName ->

                        CityTextView(
                            contentFix = cityName,
                            // Display city name
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(dimensionResource(R.dimen.medium_spacer)))
                    }

                    // Display weather icon
                    AsyncImage(
                        model = ImageRequest.Builder(currentContext)
                            .data(currentState.city?.weatherIcon).crossfade(true).build(),
                        contentDescription = "El temps que fa",
                        modifier = Modifier
                            .height(dimensionResource(R.dimen.weather_icon_size))
                            .width(
                                dimensionResource(R.dimen.weather_icon_size)
                            ),
                        contentScale = ContentScale.Crop
                    )

                    currentState.city?.weatherDescription?.let { weatherDescription ->

                        // Display weather description
                        CityTextView(
                            contentFix = weatherDescription,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    CityTextView(
                        // Display current temperature
                        contentFix = stringResource(R.string.city_current_temperature),
                        contentDynamic = currentState.city?.temperature.toString()
                    )

                    CityTextView(
                        // Display maximum temperature
                        contentFix = stringResource(R.string.city_max_temperature),
                        contentDynamic = currentState.city?.temperatureMax.toString(),
                        colorDynamic = Color.Red
                    )

                    CityTextView(
                        // Display minimum temperature
                        contentFix = stringResource(R.string.city_min_temperature),
                        contentDynamic = currentState.city?.temperatureMin.toString(),
                        colorDynamic = Color.Blue
                    )

                    CityTextView(
                        // Display pressure
                        contentFix = stringResource(R.string.city_pressure),
                        contentDynamic = currentState.city?.pressure.toString()
                    )

                    CityTextView(
                        // Display rain information
                        contentFix = stringResource(R.string.city_rain),
                        contentDynamic = currentState.city?.rain.toString(),
                        colorDynamic = Color.Blue
                    )

                }
            }

        } else {
            // If forecast is not loaded, show a placeholder or loading indicator
            // (Currently, this block is empty, consider adding a placeholder if needed)
        }

        Button(onClick = {

            coroutineScope.launch {
                getLocation()
            }

        }) {

            Text(text = stringResource(R.string.update_forecast))

        }

        Spacer(Modifier.height(dimensionResource(R.dimen.medium_spacer)))

    }

    // Show loading indicator if loading state is true
    if (currentState.loading) CircularProgressIndicatorCustom()

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
