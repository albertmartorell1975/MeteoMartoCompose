package com.martorell.albert.meteomartocompose.ui.screens.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.TemperatureAlertResult
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import com.martorell.albert.meteomartocompose.domain.model.fold
import com.martorell.albert.meteomartocompose.usecases.cityweather.CityWeatherInteractors
import com.martorell.albert.meteomartocompose.utils.AppLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

/**
 * ViewModel for the City Weather screen.
 *
 * This state holder follows the Passive Initialization Mandate: it does not trigger any
 * side effects (like coroutines or API calls) in its init block. All work is triggered
 * explicitly by the UI or through declarative streams.
 *
 * @property cityWeatherInteractors A data class containing all use cases required for this feature.
 */
@HiltViewModel
class CityWeatherViewModel @Inject constructor(
    private val cityWeatherInteractors: CityWeatherInteractors,
    private val appLogger: AppLogger,
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    private val _events = Channel<TemperatureAlertResult>(Channel.BUFFERED)
    val events: Flow<TemperatureAlertResult> = _events.receiveAsFlow()

    private val loadMutex = Mutex()
    private val monitoringMutex = Mutex()

    data class UiState(
        val content: CityContent = CityContent.Initial,
        val isLoading: Boolean = false,
        val showGPSDialog: Boolean = false,
        val locationChecked: Boolean = false,
        val showRationale: Boolean = false,
        val permissionsGranted: Boolean = false,
        val coordinates: CurrentLocationDomain = CurrentLocationDomain(),
        val logOut: Boolean = false,
        val showFab: Boolean = false,
        val isHighTempAlertActive: Boolean = false,
    )

    sealed interface CityContent {
        data object Initial : CityContent
        data object NeedsLocation : CityContent
        data class Success(val city: CityWeatherDomain) : CityContent
        data class Error(val error: DomainError) : CityContent
    }

    // --- Public Functions ---

    fun onStart() {
        // Mutex-based idempotency guard. 
        // If already loading, tryLock returns false, and we avoid duplicate work.
        if (!loadMutex.tryLock()) return

        viewModelScope.launch {
            try {
                if (state.value.content is CityContent.Initial) {
                    performInitialLoad()
                    startMonitoring()
                }
            } finally {
                loadMutex.unlock()
            }
        }
    }

    /**
     * Public refresh action, always forces a reload regardless of current state.
     */
    fun onRefresh() {
        viewModelScope.launch {
            // In a refresh, we might want to wait if another load is active, 
            // or just bypass the guard if it's a manual user action.
            // Using withLock ensures we don't overlap, but we definitely execute.
            loadMutex.lock()
            try {
                performInitialLoad()
            } finally {
                loadMutex.unlock()
            }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            val currentCity = (_state.value.content as? CityContent.Success)?.city
            currentCity?.let { city ->
                cityWeatherInteractors.switchFavoriteUseCase.invoke(city)
            }
        }
    }

    fun isCityFavorite(): Boolean {
        return (state.value.content as? CityContent.Success)?.city?.favorite ?: false
    }

    fun onLogOutClicked() {
        cityWeatherInteractors.logOutUseCase.invoke()
    }

    fun showLogOutDialog() {
        _state.update { stateUpdated ->
            stateUpdated.copy(
                logOut = true,
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

    fun gpsDialogHid() {
        _state.update {
            it.copy(
                content = CityContent.NeedsLocation,
                isLoading = false,
                showGPSDialog = false,
                coordinates = CurrentLocationDomain(),
            )
        }
    }

    fun rationaleDialogShowed() {
        _state.update {
            it.copy(
                locationChecked = true,
                showRationale = true,
            )
        }
    }

    fun rationaleDialogHid() {
        _state.update {
            it.copy(
                content = CityContent.NeedsLocation,
                isLoading = false,
                locationChecked = false,
                showRationale = false
            )
        }
    }

    fun onOpenAppSettingsClicked() {
        cityWeatherInteractors.openAppSettingsUseCase()
    }

    fun onOpenLocationSettingsClicked() {
        cityWeatherInteractors.openLocationSettingsUseCase()
    }

    /**
     * Starts monitoring temperature thresholds using a declarative stream.
     * This function should be called by the UI when it's ready to receive alerts.
     */
    fun startMonitoring() {
        // Guard to ensure only one collection is active for the lifetime of the VM.
        if (!monitoringMutex.tryLock()) return

        viewModelScope.launch {
            cityWeatherInteractors.checkTemperatureThresholdUseCase()
                .collect { result ->
                    _state.update { it.copy(isHighTempAlertActive = result.isPersistentAlertActive) }

                    appLogger.debug(
                        "TempAlert",
                        "Current Temperature: ${result.currentTemperature}°C  High Alert Threshold: ${result.threshold}°C",
                    )

                    if (result.showAlert) {
                        // Mark as notified immediately to avoid double triggers from rapid emissions
                        cityWeatherInteractors.markCityAlertNotifiedUseCase(
                            cityName = result.cityName,
                            notified = true
                        )
                        _events.send(result)
                    } else if (!result.isPersistentAlertActive) {
                        cityWeatherInteractors.markCityAlertNotifiedUseCase(
                            cityName = result.cityName,
                            notified = false
                        )
                    }
                }
        }
    }

    fun getRequiredPermissions(): List<String> = cityWeatherInteractors.getWeatherPermissionsUseCase()

    // --- Private Functions ---

    private suspend fun performInitialLoad() {
        _state.update {
            it.copy(
                isLoading = true,
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

                currentLocation.fold(
                    onError = { locationError ->
                        val permissionsGranted = locationError !is DomainError.Location.PermissionDenied
                        _state.update { updatedState ->
                            updatedState.copy(
                                content = CityContent.NeedsLocation,
                                isLoading = false,
                                showGPSDialog = permissionsGranted,
                                locationChecked = true,
                                permissionsGranted = permissionsGranted,
                            )
                        }
                    },
                    onSuccess = { location ->
                        cityWeatherInteractors.saveLocationUseCase.invoke(
                            latitude = location.latitude,
                            longitude = location.longitude,
                        )

                        _state.update { updatedState ->
                            updatedState.copy(
                                coordinates = location,
                                showGPSDialog = false,
                                locationChecked = true,
                                permissionsGranted = true,
                            )
                        }

                        loadCityWeather()
                    },
                )

            } else {

                // GPS is not enabled
                _state.update { updatedState ->
                    updatedState.copy(
                        content = CityContent.NeedsLocation,
                        isLoading = false,
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
                    content = CityContent.NeedsLocation,
                    isLoading = false,
                    permissionsGranted = false,
                    locationChecked = true
                )
            }

        }
    }

    private suspend fun loadCityWeather() {
        val loadResult = cityWeatherInteractors.loadCityWeatherByCoordinatesUseCase.invoke(
            latitude = _state.value.coordinates.latitude.toString(),
            longitude = _state.value.coordinates.longitude.toString()
        )

        when (loadResult) {
            is Result.Error -> {
                _state.update { stateUpdated ->
                    stateUpdated.copy(
                        content = CityContent.Error(error = loadResult.error),
                        isLoading = false,
                        locationChecked = true,
                        showFab = false
                    )
                }
            }

            is Result.Success -> {
                cityWeatherInteractors.getAllCitiesUseCase.invoke()
                    .catch {
                        _state.update { stateUpdated ->
                            stateUpdated.copy(
                                content = CityContent.Error(error = DomainError.Local.DatabaseError),
                                isLoading = false,
                                showFab = false
                            )
                        }
                    }
                    .collect { listOfCities ->
                        val city = listOfCities.find { it.justAdded }
                        _state.update { stateUpdated ->
                            stateUpdated.copy(
                                content = if (city != null) CityContent.Success(city) else stateUpdated.content,
                                isLoading = false,
                                showFab = listOfCities.isNotEmpty()
                            )
                        }
                    }
            }
        }
    }
}
