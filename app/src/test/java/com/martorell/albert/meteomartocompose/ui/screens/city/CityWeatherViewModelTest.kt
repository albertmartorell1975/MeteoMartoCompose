package com.martorell.albert.meteomartocompose.ui.screens.city

import android.util.Log
import arrow.core.right
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.TemperatureAlertResult
import com.martorell.albert.meteomartocompose.usecases.cityweather.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CityWeatherViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val checkLocationPermissionsUseCase: CheckLocationPermissionsUseCase = mockk()
    private val checkNotificationPermissionUseCase: CheckNotificationPermissionUseCase = mockk()
    private val currentLocationUseCase: CurrentLocationUseCase = mockk()
    private val isGPSEnableUseCase: IsGPSEnableUseCase = mockk()
    private val loadCityWeatherByCoordinatesUseCase: LoadCityWeatherByCoordinatesUseCase = mockk()
    private val switchFavoriteUseCase: SwitchFavoriteUseCase = mockk()
    private val getAllCitiesUseCase: GetAllCitiesUseCase = mockk()
    private val isCurrentCityFavoriteUseCase: IsCurrentCityFavoriteUseCase = mockk()
    private val logOutUseCase: LogOutUseCase = mockk()
    private val saveLocationUseCase: SaveLocationUseCase = mockk()
    private val checkTemperatureThresholdUseCase: CheckTemperatureThresholdUseCase = mockk()
    private val markCityAlertNotifiedUseCase: MarkCityAlertNotifiedUseCase = mockk()
    private val getWeatherPermissionsUseCase: GetWeatherPermissionsUseCase = mockk()
    private val openAppSettingsUseCase: OpenAppSettingsUseCase = mockk()
    private val openLocationSettingsUseCase: OpenLocationSettingsUseCase = mockk()

    private val interactors = CityWeatherInteractors(
        checkLocationPermissionsUseCase,
        checkNotificationPermissionUseCase,
        currentLocationUseCase,
        isGPSEnableUseCase,
        loadCityWeatherByCoordinatesUseCase,
        switchFavoriteUseCase,
        getAllCitiesUseCase,
        isCurrentCityFavoriteUseCase,
        logOutUseCase,
        saveLocationUseCase,
        checkTemperatureThresholdUseCase,
        markCityAlertNotifiedUseCase,
        getWeatherPermissionsUseCase,
        openAppSettingsUseCase,
        openLocationSettingsUseCase
    )

    private lateinit var viewModel: CityWeatherViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        viewModel = CityWeatherViewModel(interactors)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `initial state is correct and passive`() {
        val state = viewModel.state.value
        assertFalse(state.loading)
        assertFalse(state.loadedForecast)
        assertNull(state.city)
        verify { checkTemperatureThresholdUseCase wasNot Called }
    }

    @Test
    fun `getCurrentLocationStarted success flow`() = runTest {
        val mockCity = CityWeatherDomain(
            name = "Barcelona",
            justAdded = true,
            temperature = 25.0,
            temperatureMin = 20.0,
            temperatureMax = 30.0,
            pressure = 1013
        )
        
        coEvery { checkLocationPermissionsUseCase() } returns true
        coEvery { checkNotificationPermissionUseCase() } returns true
        coEvery { isGPSEnableUseCase() } returns true
        coEvery { currentLocationUseCase() } returns CurrentLocationDomain(41.38, 2.17).right()
        coEvery { saveLocationUseCase(any(), any()) } just Runs
        coEvery { loadCityWeatherByCoordinatesUseCase(any(), any()) } returns null
        coEvery { getAllCitiesUseCase() } returns flowOf(listOf(mockCity))

        viewModel.getCurrentLocationStarted()
        
        // With UnconfinedTestDispatcher, it runs until suspension.
        // But since mocked use cases might return immediately, it might have finished already.
        // So we check the final state.
        
        val finalState = viewModel.state.value
        assertFalse(finalState.loading)
        assertTrue(finalState.loadedForecast)
        assertEquals("Barcelona", finalState.city?.name)
        
        coVerify { currentLocationUseCase() }
        coVerify { loadCityWeatherByCoordinatesUseCase("41.38", "2.17") }
    }

    @Test
    fun `getCurrentLocationStarted shows GPS dialog when GPS disabled`() = runTest {
        coEvery { checkLocationPermissionsUseCase() } returns true
        coEvery { checkNotificationPermissionUseCase() } returns true
        coEvery { isGPSEnableUseCase() } returns false

        viewModel.getCurrentLocationStarted()

        val state = viewModel.state.value
        assertTrue(state.showGPSDialog)
        assertTrue(state.locationChecked)
        assertFalse(state.loading)
    }

    @Test
    fun `startMonitoring updates high temperature alert state`() = runTest {
        val alertResult = TemperatureAlertResult(
            cityName = "Barcelona",
            showAlert = false,
            isPersistentAlertActive = true,
            currentTemperature = 35.0,
            threshold = 30.0
        )
        every { checkTemperatureThresholdUseCase() } returns flowOf(alertResult)
        coEvery { markCityAlertNotifiedUseCase(any(), any()) } just Runs

        viewModel.startMonitoring()

        assertTrue(viewModel.state.value.isHighTempAlertActive)
    }

    @Test
    fun `onFavoriteClicked calls switchFavoriteUseCase`() = runTest {
        val city = CityWeatherDomain(
            name = "Sabadell",
            justAdded = true,
            temperature = 25.0,
            temperatureMin = 20.0,
            temperatureMax = 30.0,
            pressure = 1013
        )
        
        coEvery { checkLocationPermissionsUseCase() } returns true
        coEvery { checkNotificationPermissionUseCase() } returns true
        coEvery { isGPSEnableUseCase() } returns true
        coEvery { currentLocationUseCase() } returns CurrentLocationDomain(0.0, 0.0).right()
        coEvery { saveLocationUseCase(any(), any()) } just Runs
        coEvery { loadCityWeatherByCoordinatesUseCase(any(), any()) } returns null
        coEvery { getAllCitiesUseCase() } returns flowOf(listOf(city))

        viewModel.getCurrentLocationStarted()
        
        coEvery { switchFavoriteUseCase(any()) } just Runs

        viewModel.onFavoriteClicked()

        coVerify { switchFavoriteUseCase(match { it.name == "Sabadell" }) }
    }
}
