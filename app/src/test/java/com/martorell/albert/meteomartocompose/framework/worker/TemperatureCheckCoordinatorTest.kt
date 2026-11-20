package com.martorell.albert.meteomartocompose.framework.worker

import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionChecker
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.testutil.fakes.FakeCityWeatherRepository
import com.martorell.albert.meteomartocompose.testutil.fakes.FakeNotificationService
import com.martorell.albert.meteomartocompose.testutil.fakes.FakePermissionChecker
import com.martorell.albert.meteomartocompose.testutil.fakes.FakeRemoteConfigRepository
import com.martorell.albert.meteomartocompose.usecases.cityweather.CheckTemperatureThresholdUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.GetAllCitiesUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.LoadCityWeatherByCoordinatesUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.MarkCityAlertNotifiedUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TemperatureCheckCoordinatorTest {

    private val cityWeatherRepository = FakeCityWeatherRepository()
    private val remoteConfigRepository = FakeRemoteConfigRepository(initialThreshold = 30.0)
    private val notificationService = FakeNotificationService()
    private val permissionChecker = FakePermissionChecker()

    private val coordinator = TemperatureCheckCoordinator(
        getAllCitiesUseCase = GetAllCitiesUseCase(cityWeatherRepository),
        loadCityWeatherByCoordinatesUseCase = LoadCityWeatherByCoordinatesUseCase(cityWeatherRepository),
        checkTemperatureThresholdUseCase = CheckTemperatureThresholdUseCase(
            cityWeatherRepository,
            remoteConfigRepository,
        ),
        markCityAlertNotifiedUseCase = MarkCityAlertNotifiedUseCase(cityWeatherRepository),
        notificationService = notificationService,
        permissionChecker = permissionChecker,
    )

    @Test
    fun `returns completed without refreshing when permission is denied or no monitored city exists`() =
        runTest {
            val deniedOutcome = coordinator.execute()
            assertEquals(TemperatureCheckOutcome.COMPLETED, deniedOutcome)
            assertEquals(0, cityWeatherRepository.listOfCitiesAccessCount)
            assertTrue(cityWeatherRepository.loadCurrentWeatherCalls.isEmpty())

            permissionChecker.grantedPermissions += PermissionChecker.Permission.POST_NOTIFICATIONS
            cityWeatherRepository.setCities(
                listOf(
                    CityWeatherDomain(
                        name = "Favorite Only",
                        temperature = 35.0,
                        justAdded = false,
                        pressure = 1013,
                        temperatureMin = 30.0,
                        temperatureMax = 40.0,
                    )
                )
            )

            val noMonitoredCityOutcome = coordinator.execute()
            assertEquals(TemperatureCheckOutcome.COMPLETED, noMonitoredCityOutcome)
            assertTrue(cityWeatherRepository.loadCurrentWeatherCalls.isEmpty())
            assertTrue(notificationService.shownTemperatures.isEmpty())
        }

    @Test
    fun `refreshes weather shows notification and marks notified when threshold alert triggers`() =
        runTest {
            permissionChecker.grantedPermissions += PermissionChecker.Permission.POST_NOTIFICATIONS
            cityWeatherRepository.setCities(
                listOf(
                    CityWeatherDomain(
                        name = "Hot City",
                        temperature = 35.0,
                        justAdded = true,
                        pressure = 1013,
                        temperatureMin = 30.0,
                        temperatureMax = 40.0,
                        latitude = 1.0,
                        longitude = 2.0,
                        isAlertNotified = false,
                    )
                )
            )

            val outcome = coordinator.execute()

            assertEquals(TemperatureCheckOutcome.COMPLETED, outcome)
            assertEquals(listOf("1.0" to "2.0"), cityWeatherRepository.loadCurrentWeatherCalls)
            assertEquals(listOf(35.0), notificationService.shownTemperatures)
            assertEquals(listOf("Hot City" to true), cityWeatherRepository.updatedAlertStatuses)
        }

    @Test
    fun `resets notified status when alert clears and returns retry on unexpected error`() =
        runTest {
            permissionChecker.grantedPermissions += PermissionChecker.Permission.POST_NOTIFICATIONS
            cityWeatherRepository.setCities(
                listOf(
                    CityWeatherDomain(
                        name = "Cool City",
                        temperature = 22.0,
                        justAdded = true,
                        pressure = 1013,
                        temperatureMin = 18.0,
                        temperatureMax = 25.0,
                        latitude = 41.38,
                        longitude = 2.17,
                        isAlertNotified = true,
                    )
                )
            )

            val resetOutcome = coordinator.execute()
            assertEquals(TemperatureCheckOutcome.COMPLETED, resetOutcome)
            assertTrue(notificationService.shownTemperatures.isEmpty())
            assertEquals(listOf("Cool City" to false), cityWeatherRepository.updatedAlertStatuses)

            cityWeatherRepository.loadCurrentWeatherException = IllegalStateException("Network boom")
            val retryOutcome = coordinator.execute()
            assertEquals(TemperatureCheckOutcome.RETRY, retryOutcome)
        }
}
