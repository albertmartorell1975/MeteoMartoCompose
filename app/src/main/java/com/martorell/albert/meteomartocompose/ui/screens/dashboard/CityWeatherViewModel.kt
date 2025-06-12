package com.martorell.albert.meteomartocompose.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import com.martorell.albert.meteomartocompose.usecases.cityweather.CityWeatherInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
       // val gpsEnabled: Boolean = false,
        val showGPSDialog: Boolean = false,
        val locationChecked: Boolean = false,
        val showRationale: Boolean = false,
        val permissionsGranted: Boolean = false,
        val error: CustomError? = null,
        val coordinates: ResultResponse<CurrentLocationDomain> = Either.Right(CurrentLocationDomain())
    )

    init {

        viewModelScope.launch {

            getCurrentLocationStarted()

        }

    }

    fun gpsDialogHid() {

        val tmpState = _state.value
        val updatedState = tmpState.copy(
            loading = false,
            //gpsEnabled = false,
            showGPSDialog = false,
            error = null,
            coordinates = Either.Right(CurrentLocationDomain())
        )
        _state.value = updatedState

    }

    fun rationaleDialogShowed() {

        val tmpState = _state.value
        val updatedState = tmpState.copy(
            locationChecked = true,
            showRationale = true
        )
        _state.value = updatedState

    }

    fun rationaleDialogHid() {

        val tmpState = _state.value
        val updatedState = tmpState.copy(
            loading = false,
            error = null,
            locationChecked = false,
            showRationale = false
        )

        _state.value = updatedState

    }

    suspend fun getCurrentLocationStarted() {

        val tmpState = _state.value
        var updatedState = tmpState.copy(
            loading = true,
            error = null,
            locationChecked = false
        )
        _state.value = updatedState

        if (cityWeatherInteractors.checkLocationPermissionsUseCase.invoke()) {

            if (cityWeatherInteractors.isGPSEnableUseCase.invoke()) {

                val result = cityWeatherInteractors.currentLocationUseCase.invoke()

                result.fold({
                    updatedState = tmpState.copy(
                        loading = false,
                        //gpsEnabled = false,
                        showGPSDialog = true,
                        error = it,
                        locationChecked = true
                    )
                    _state.value = updatedState

                }) {
                    updatedState = tmpState.copy(
                        loading = false,
                        //gpsEnabled = true,
                        coordinates = result,
                        showGPSDialog = false,
                        error = null,
                        locationChecked = true
                    )
                    _state.value = updatedState
                }

            } else {

                // gps is not enabled
                updatedState = tmpState.copy(
                    loading = false,
                    error = null,
                    permissionsGranted = true,
                    locationChecked = true,
                    //gpsEnabled = false,
                    showGPSDialog = true
                )
                _state.value = updatedState

            }

        } else {

            // permissions are not granted
            updatedState = tmpState.copy(
                loading = false,
                error = null,
                permissionsGranted = false,
                locationChecked = true,
                //gpsEnabled = false
            )
            _state.value = updatedState

        }

    }

}