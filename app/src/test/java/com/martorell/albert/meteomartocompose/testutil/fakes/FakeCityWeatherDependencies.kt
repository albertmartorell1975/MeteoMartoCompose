package com.martorell.albert.meteomartocompose.testutil.fakes

import arrow.core.left
import arrow.core.right
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.data.CustomErrorFlow
import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.city.repositories.CityWeatherRepository
import com.martorell.albert.meteomartocompose.data.city.repositories.LocationRepository
import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionChecker
import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionRepository
import com.martorell.albert.meteomartocompose.data.city.sources.SystemService
import com.martorell.albert.meteomartocompose.data.notification.NotificationService
import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepository
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import com.martorell.albert.meteomartocompose.utils.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class FakeCityWeatherRepository(
    var streamCitiesContinuously: Boolean = false,
) : CityWeatherRepository {

    private val citiesFlow = MutableStateFlow<List<CityWeatherDomain>>(emptyList())

    var loadCurrentWeatherError: CustomErrorFlow? = null
    var loadCurrentWeatherException: Throwable? = null
    var onLoadCurrentWeather: ((String, String) -> Unit)? = null
    var loadCityByNameResult: ResultResponse<CityWeatherDomain> =
        CustomError.Unknown("Not configured").left()
    var currentCityFavorite: Boolean = false

    var listOfCitiesAccessCount: Int = 0
        private set
    val loadCurrentWeatherCalls = mutableListOf<Pair<String, String>>()
    val loadCityByNameCalls = mutableListOf<String>()
    val switchedFavoriteCities = mutableListOf<CityWeatherDomain>()
    val removedFavoriteCityNames = mutableListOf<String>()
    val updatedAlertStatuses = mutableListOf<Pair<String, Boolean>>()

    override val listOfCities: Flow<List<CityWeatherDomain>>
        get() {
            listOfCitiesAccessCount++
            return if (streamCitiesContinuously) {
                citiesFlow
            } else {
                flow { emit(citiesFlow.value) }
            }
        }

    fun setCities(cities: List<CityWeatherDomain>) {
        citiesFlow.value = cities
    }

    override suspend fun loadCityCurrentWeather(
        latitude: String,
        longitude: String,
    ): CustomErrorFlow? {
        loadCurrentWeatherCalls += latitude to longitude
        loadCurrentWeatherException?.let { throw it }
        onLoadCurrentWeather?.invoke(latitude, longitude)
        return loadCurrentWeatherError
    }

    override suspend fun switchFavorite(city: CityWeatherDomain) {
        switchedFavoriteCities += city
        citiesFlow.value = citiesFlow.value.map { existing ->
            if (existing.name == city.name) existing.copy(favorite = !existing.favorite) else existing
        }
    }

    override suspend fun isCurrentCityFavorite(): Boolean = currentCityFavorite

    override suspend fun removeCityAsFavorite(cityName: String) {
        removedFavoriteCityNames += cityName
        citiesFlow.value = citiesFlow.value.map { existing ->
            if (existing.name == cityName) existing.copy(favorite = false) else existing
        }
    }

    override suspend fun loadCityByName(cityName: String): ResultResponse<CityWeatherDomain> {
        loadCityByNameCalls += cityName
        return loadCityByNameResult
    }

    override suspend fun updateAlertStatus(name: String, isNotified: Boolean) {
        updatedAlertStatuses += name to isNotified
        citiesFlow.value = citiesFlow.value.map { existing ->
            if (existing.name == name) existing.copy(isAlertNotified = isNotified) else existing
        }
    }
}

class FakeRemoteConfigRepository(
    initialThreshold: Double = 30.0,
    initialInterval: Long = 60L,
) : RemoteConfigRepository {

    val thresholdFlow = MutableStateFlow(initialThreshold)
    val intervalFlow = MutableStateFlow(initialInterval)

    override fun getTemperatureThreshold(): Flow<Double> = thresholdFlow

    override fun getWeatherCheckInterval(): Flow<Long> = intervalFlow
}

class FakeLocationRepository : LocationRepository {

    var currentLocationResult: ResultResponse<CurrentLocationDomain> =
        CurrentLocationDomain(latitude = 41.38, longitude = 2.17).right()
    var loadCurrentLocationCallCount: Int = 0
        private set
    val savedLocations = mutableListOf<Pair<Double?, Double?>>()

    override suspend fun loadCurrentLocation(): ResultResponse<CurrentLocationDomain> {
        loadCurrentLocationCallCount++
        return currentLocationResult
    }

    override suspend fun saveLocation(latitude: Double?, longitude: Double?) {
        savedLocations += latitude to longitude
    }
}

class FakePermissionRepository : PermissionRepository {

    var locationPermissionsGranted: Boolean = true
    var notificationPermissionGranted: Boolean = true
    var gpsEnabled: Boolean = true
    var requiredWeatherPermissions: List<String> = emptyList()

    override suspend fun checkLocationPermissions(): Boolean = locationPermissionsGranted

    override suspend fun checkNotificationPermission(): Boolean = notificationPermissionGranted

    override suspend fun isGPSEnabled(): Boolean = gpsEnabled

    override fun getWeatherPermissions(): List<String> = requiredWeatherPermissions
}

class FakePermissionChecker(
    initialPermissions: Set<PermissionChecker.Permission> = emptySet(),
) : PermissionChecker {

    val grantedPermissions = initialPermissions.toMutableSet()
    var gpsEnabled: Boolean = true
    var requiredWeatherPermissions: List<String> = emptyList()

    override suspend fun check(permission: PermissionChecker.Permission): Boolean =
        permission in grantedPermissions

    override suspend fun isGPSEnabled(): Boolean = gpsEnabled

    override fun getWeatherPermissions(): List<String> = requiredWeatherPermissions
}

class FakeNotificationService : NotificationService {

    val shownTemperatures = mutableListOf<Double>()

    override suspend fun showHighTemperatureNotification(temperature: Double) {
        shownTemperatures += temperature
    }
}

class FakeSystemService : SystemService {

    var openAppSettingsCallCount: Int = 0
        private set
    var openLocationSettingsCallCount: Int = 0
        private set

    override fun openAppSettings() {
        openAppSettingsCallCount++
    }

    override fun openLocationSettings() {
        openLocationSettingsCallCount++
    }
}

class FakeAppLogger : AppLogger {

    data class LogEntry(
        val tag: String,
        val message: String,
        val throwable: Throwable? = null,
    )

    val debugLogs = mutableListOf<LogEntry>()
    val errorLogs = mutableListOf<LogEntry>()

    override fun debug(tag: String, message: String) {
        debugLogs += LogEntry(tag = tag, message = message)
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        errorLogs += LogEntry(tag = tag, message = message, throwable = throwable)
    }
}
