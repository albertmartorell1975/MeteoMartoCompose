package com.martorell.albert.meteomartocompose.ui.screens.city

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.data.CustomErrorFlow
import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.toCustomErrorFlow
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.TemperatureAlertResult
import com.martorell.albert.meteomartocompose.usecases.cityweather.CityWeatherInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityWeatherViewModel @Inject constructor(
    private val cityWeatherInteractors: CityWeatherInteractors,
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    private val _events = Channel<TemperatureAlertResult>(Channel.BUFFERED)
    val events: Flow<TemperatureAlertResult> = _events.receiveAsFlow()

    data class UiState(
        val loading: Boolean = false,
        val showGPSDialog: Boolean = false,
        val locationChecked: Boolean = false,
        val showRationale: Boolean = false,
        val permissionsGranted: Boolean = false,
        val errorLocation: CustomError? = null,
        val errorForecast: CustomErrorFlow? = null,
        val coordinates: ResultResponse<CurrentLocationDomain> = Either.Right(CurrentLocationDomain()),
        val city: CityWeatherDomain? = null,
        val loadedForecast: Boolean = false,
        val logOut: Boolean = false,
        val showFab: Boolean = false,
        val isHighTempAlertActive: Boolean = false,
    )

    init {

        viewModelScope.launch {

            getCurrentLocationStarted()

        }

        viewModelScope.launch {
            cityWeatherInteractors.checkTemperatureThresholdUseCase()
                .collect { result ->
                    Log.d(
                        "TempAlert",
                        "Current Temperature: ${result.currentTemperature}°C  High Alert Threshold: ${result.threshold}°C",
                    )
                    
                    _state.update { it.copy(isHighTempAlertActive = result.isPersistentAlertActive) }

                    if (result.showAlert) {
                        _events.send(result)
                        cityWeatherInteractors.markCityAlertNotifiedUseCase(result.cityName, true)
                    } else if (!result.isPersistentAlertActive) {
                        cityWeatherInteractors.markCityAlertNotifiedUseCase(result.cityName, false)
                    }
                }
        }

    }

    fun gpsDialogHid() {

        _state.update {
            it.copy(
                loading = false,
                showGPSDialog = false,
                errorLocation = null,
                coordinates = Either.Right(CurrentLocationDomain())
            )
        }

    }

    fun rationaleDialogShowed() {

        _state.update {
            it.copy(
                locationChecked = true,
                showRationale = true
            )
        }

    }

    fun rationaleDialogHid() {

        _state.update {
            it.copy(
                loading = false,
                errorLocation = null,
                locationChecked = false,
                showRationale = false
            )
        }

    }

    suspend fun getCurrentLocationStarted() {

        _state.update {
            it.copy(
                loading = true,
                errorLocation = null,
                locationChecked = false,
                showGPSDialog = false,
                showRationale = false
            )
        }

        val locationGranted = cityWeatherInteractors.checkLocationPermissionsUseCase.invoke()
        val notificationsGranted =
            cityWeatherInteractors.checkNotificationPermissionUseCase.invoke()
        val allPermissionsGranted = locationGranted && notificationsGranted

        if (allPermissionsGranted) {

            if (cityWeatherInteractors.isGPSEnableUseCase.invoke()) {

                val currentLocation = cityWeatherInteractors.currentLocationUseCase.invoke()

                currentLocation.fold({

                    // current location not loaded
                    _state.update { updatedState ->
                        updatedState.copy(
                            loading = false,
                            showGPSDialog = true,
                            errorLocation = it,
                            locationChecked = true,
                            permissionsGranted = true,
                        )
                    }

                }) {

                    // current location loaded
                    cityWeatherInteractors.saveLocationUseCase.invoke(
                        latitude = it.latitude, longitude = it.longitude
                    )

                    _state.update { updatedState ->
                        updatedState.copy(
                            loading = true,
                            coordinates = currentLocation,
                            showGPSDialog = false,
                            errorLocation = null,
                            errorForecast = null,
                            locationChecked = true,
                            permissionsGranted = true,
                        )
                    }

                    loadCityWeather()

                }

            } else {

                // GPS is not enabled
                _state.update { updatedState ->
                    updatedState.copy(
                        loading = false,
                        errorLocation = null,
                        permissionsGranted = true,
                        locationChecked = true,
                        showGPSDialog = true
                    )
                }

            }

        } else {

            // Permissions are not granted
            _state.update { updatedState ->
                updatedState.copy(
                    loading = false,
                    errorLocation = null,
                    permissionsGranted = false,
                    locationChecked = true
                )
            }

        }

    }

    private suspend fun loadCityWeather() {

        val errorLoadForecast = cityWeatherInteractors.loadCityWeatherByCoordinatesUseCase.invoke(
            latitude = _state.value.coordinates.getOrNull()?.latitude.toString(),
            longitude = _state.value.coordinates.getOrNull()?.longitude.toString()
        )

        errorLoadForecast?.also {
            _state.update { stateUpdated ->
                stateUpdated.copy(
                    loading = false,
                    errorForecast = errorLoadForecast,
                    loadedForecast = true,
                    locationChecked = true,
                    showFab = false
                )
            }
        } ?: run {
            cityWeatherInteractors.getAllCitiesUseCase.invoke().catch { cause ->
                _state.update { stateUpdated ->
                    stateUpdated.copy(
                        loading = false,
                        errorForecast = cause.toCustomErrorFlow(),
                        city = null,
                        showFab = false
                    )
                }
            }.collect { listOfCities ->
                _state.update { stateUpdated ->
                    stateUpdated.copy(
                        loading = false,
                        errorForecast = null,
                        loadedForecast = true,
                        city = listOfCities.find { city -> city.justAdded },
                        showFab = listOfCities.isNotEmpty()
                    )
                }

            }

        }

    }

    fun onFavoriteClicked() {

        viewModelScope.launch {
            _state.value.city?.let { city ->
                cityWeatherInteractors.switchFavoriteUseCase.invoke(city)
            }
        }

    }

    suspend fun isCityFavorite(): Boolean {

        return cityWeatherInteractors.isCurrentCityFavoriteUseCase.invoke()

    }

    fun onLogOutClicked() {

        cityWeatherInteractors.logOutUseCase.invoke()

    }

    fun showLogOutDialog() {

        _state.update { stateUpdated ->
            stateUpdated.copy(
                logOut = true
            )
        }

    }

    fun hideLogOutDialog() {

        _state.update { stateUpdated ->
            stateUpdated.copy(
                logOut = false
            )
        }

    }

    fun getRequiredPermissions(): List<String> = cityWeatherInteractors.getWeatherPermissionsUseCase()

}
