package com.martorell.albert.meteomartocompose.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import com.martorell.albert.meteomartocompose.usecases.cityweather.CityWeatherInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityWeatherViewModel @Inject constructor(
    private val cityWeatherInteractors: CityWeatherInteractors
) :
    ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    data class UiState(
        val loading: Boolean = false,
        val showGPSDialog: Boolean = false,
        val locationChecked: Boolean = false,
        val showRationale: Boolean = false,
        val permissionsGranted: Boolean = false,
        val error: CustomError? = null,
        val coordinates: ResultResponse<CurrentLocationDomain> =
            Either.Right(CurrentLocationDomain()),
        val city: CityWeatherDomain? = null,
        val loadedForecast: Boolean = false
    )

    init {

        viewModelScope.launch {

            getCurrentLocationStarted()

        }

    }

    fun gpsDialogHid() {

        _state.update {
            it.copy(
                loading = false,
                showGPSDialog = false,
                error = null,
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
                error = null,
                locationChecked = false,
                showRationale = false
            )
        }

    }

    suspend fun getCurrentLocationStarted() {

        _state.update {
            it.copy(
                loading = true,
                error = null,
                locationChecked = false
            )
        }

        if (cityWeatherInteractors.checkLocationPermissionsUseCase.invoke()) {

            if (cityWeatherInteractors.isGPSEnableUseCase.invoke()) {

                val result = cityWeatherInteractors.currentLocationUseCase.invoke()

                result.fold({

                    // current location not loaded
                    _state.update { updatedState ->
                        updatedState.copy(
                            loading = false,
                            showGPSDialog = true,
                            error = it,
                            locationChecked = true
                        )
                    }

                }) {

                    // current location loaded
                    _state.update { updatedState ->
                        updatedState.copy(
                            loading = false,
                            coordinates = result,
                            showGPSDialog = false,
                            error = null,
                            locationChecked = true
                        )
                    }

                }

            } else {

                // gps is not enabled
                _state.update { updatedState ->
                    updatedState.copy(
                        loading = false,
                        error = null,
                        permissionsGranted = true,
                        locationChecked = true,
                        showGPSDialog = true
                    )
                }

            }

        } else {

            // permissions are not granted
            _state.update { updatedState ->
                updatedState.copy(
                    loading = false,
                    error = null,
                    permissionsGranted = false,
                    locationChecked = true
                )
            }

        }

    }

    suspend fun loadCityWeather() {

        _state.value.coordinates.fold({}) {

            val error = cityWeatherInteractors.loadCityForecastUseCase.invoke(
                it.latitude.toString(),
                it.longitude.toString()
            )

            _state.update { stateUpdated ->
                stateUpdated.copy(
                    error = error.leftOrNull(),
                    loadedForecast = true
                )
            }

        }

    }

}