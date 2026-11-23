package com.martorell.albert.meteomartocompose.ui.screens.city

import app.cash.turbine.test
import arrow.core.right
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import com.martorell.albert.meteomartocompose.testutil.MainDispatcherRule
import com.martorell.albert.meteomartocompose.testutil.fakes.FakeAppLogger
import com.martorell.albert.meteomartocompose.testutil.fakes.FakeAuthRepository
import com.martorell.albert.meteomartocompose.testutil.fakes.FakeCityWeatherRepository
import com.martorell.albert.meteomartocompose.testutil.fakes.FakeLocationRepository
import com.martorell.albert.meteomartocompose.testutil.fakes.FakePermissionRepository
import com.martorell.albert.meteomartocompose.testutil.fakes.FakeRemoteConfigRepository
import com.martorell.albert.meteomartocompose.testutil.fakes.FakeSystemService
import com.martorell.albert.meteomartocompose.usecases.cityweather.CheckLocationPermissionsUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.CheckNotificationPermissionUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.CheckTemperatureThresholdUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.CityWeatherInteractors
import com.martorell.albert.meteomartocompose.usecases.cityweather.CurrentLocationUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.GetAllCitiesUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.GetWeatherPermissionsUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.IsCurrentCityFavoriteUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.IsGPSEnableUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.LoadCityWeatherByCoordinatesUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.LogOutUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.MarkCityAlertNotifiedUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.OpenAppSettingsUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.OpenLocationSettingsUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.SaveLocationUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.SwitchFavoriteUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CityWeatherViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = FakeAuthRepository()
    private val cityWeatherRepository = FakeCityWeatherRepository()
    private val locationRepository = FakeLocationRepository()
    private val permissionRepository = FakePermissionRepository()
    private val remoteConfigRepository = FakeRemoteConfigRepository(initialThreshold = 30.0)
    private val systemService = FakeSystemService()
    private val appLogger = FakeAppLogger()

    private fun createViewModel(): CityWeatherViewModel = CityWeatherViewModel(
        cityWeatherInteractors = CityWeatherInteractors(
            checkLocationPermissionsUseCase = CheckLocationPermissionsUseCase(permissionRepository),
            checkNotificationPermissionUseCase = CheckNotificationPermissionUseCase(permissionRepository),
            currentLocationUseCase = CurrentLocationUseCase(locationRepository),
            isGPSEnableUseCase = IsGPSEnableUseCase(permissionRepository),
            loadCityWeatherByCoordinatesUseCase = LoadCityWeatherByCoordinatesUseCase(cityWeatherRepository),
            switchFavoriteUseCase = SwitchFavoriteUseCase(cityWeatherRepository),
            getAllCitiesUseCase = GetAllCitiesUseCase(cityWeatherRepository),
            isCurrentCityFavoriteUseCase = IsCurrentCityFavoriteUseCase(cityWeatherRepository),
            logOutUseCase = LogOutUseCase(authRepository),
            saveLocationUseCase = SaveLocationUseCase(locationRepository),
            checkTemperatureThresholdUseCase = CheckTemperatureThresholdUseCase(
                cityWeatherRepository,
                remoteConfigRepository,
            ),
            markCityAlertNotifiedUseCase = MarkCityAlertNotifiedUseCase(cityWeatherRepository),
            getWeatherPermissionsUseCase = GetWeatherPermissionsUseCase(permissionRepository),
            openAppSettingsUseCase = OpenAppSettingsUseCase(systemService),
            openLocationSettingsUseCase = OpenLocationSettingsUseCase(systemService),
        ),
        appLogger = appLogger,
    )

    @Test
    fun `initial state is passive onStart loads once idempotently and onRefresh reloads`() = runTest {
        val city = CityWeatherDomain(
            name = "Barcelona",
            justAdded = true,
            temperature = 25.0,
            temperatureMin = 20.0,
            temperatureMax = 30.0,
            pressure = 1013,
        )
        locationRepository.currentLocationResult = CurrentLocationDomain(41.38, 2.17).right()
        cityWeatherRepository.setCities(listOf(city))

        val viewModel = createViewModel()
        assertEquals(CityWeatherViewModel.CityContent.Initial, viewModel.state.value.content)
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(0, locationRepository.loadCurrentLocationCallCount)
        assertEquals(0, cityWeatherRepository.listOfCitiesAccessCount)

        viewModel.onStart()
        val loadedContent = viewModel.state.value.content as CityWeatherViewModel.CityContent.Success
        assertEquals("Barcelona", loadedContent.city.name)
        assertEquals(1, locationRepository.loadCurrentLocationCallCount)
        assertEquals(listOf("41.38" to "2.17"), cityWeatherRepository.loadCurrentWeatherCalls)

        viewModel.onStart()
        assertEquals(1, locationRepository.loadCurrentLocationCallCount)

        viewModel.onRefresh()
        assertEquals(2, locationRepository.loadCurrentLocationCallCount)
    }

    @Test
    fun `onStart shows GPS dialog when disabled and onFavoriteClicked toggles loaded city`() = runTest {
        permissionRepository.gpsEnabled = false
        val viewModel = createViewModel()

        viewModel.onStart()
        assertTrue(viewModel.state.value.showGPSDialog)
        assertTrue(viewModel.state.value.locationChecked)
        assertEquals(CityWeatherViewModel.CityContent.NeedsLocation, viewModel.state.value.content)

        val city = CityWeatherDomain(
            name = "Sabadell",
            justAdded = true,
            temperature = 25.0,
            temperatureMin = 20.0,
            temperatureMax = 30.0,
            pressure = 1013,
        )
        permissionRepository.gpsEnabled = true
        cityWeatherRepository.setCities(listOf(city))
        viewModel.onRefresh()
        viewModel.onFavoriteClicked()

        assertEquals(listOf("Sabadell"), cityWeatherRepository.switchedFavoriteCities.map { it.name })
    }

    @Test
    fun `startMonitoring updates persistent high temperature alert state and marks city notified`() =
        runTest {
            cityWeatherRepository.setCities(
                listOf(
                    CityWeatherDomain(
                        name = "Barcelona",
                        justAdded = true,
                        temperature = 35.0,
                        temperatureMin = 28.0,
                        temperatureMax = 37.0,
                        pressure = 1012,
                        isAlertNotified = false,
                    )
                )
            )
            val viewModel = createViewModel()

            viewModel.events.test {
                viewModel.startMonitoring()

                val event = awaitItem()
                assertTrue(event.showAlert)
                assertEquals("Barcelona", event.cityName)
                assertTrue(viewModel.state.value.isHighTempAlertActive)
                assertEquals(listOf("Barcelona" to true), cityWeatherRepository.updatedAlertStatuses)
                assertEquals(
                    listOf(
                        FakeAppLogger.LogEntry(
                            tag = "TempAlert",
                            message = "Current Temperature: 35.0°C  High Alert Threshold: 30.0°C",
                        )
                    ),
                    appLogger.debugLogs,
                )
            }
        }
}
