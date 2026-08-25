package com.martorell.albert.meteomartocompose.framework.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.PermissionChecker
import com.martorell.albert.meteomartocompose.data.notification.NotificationService
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.TemperatureAlertResult
import com.martorell.albert.meteomartocompose.usecases.cityweather.CheckTemperatureThresholdUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.GetAllCitiesUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.LoadCityWeatherByCoordinatesUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.MarkCityAlertNotifiedUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TemperatureCheckWorkerTest {

    private lateinit var context: Context
    private val getAllCitiesUseCase: GetAllCitiesUseCase = mockk()
    private val loadCityWeatherByCoordinatesUseCase: LoadCityWeatherByCoordinatesUseCase = mockk(relaxed = true)
    private val checkTemperatureThresholdUseCase: CheckTemperatureThresholdUseCase = mockk()
    private val markCityAlertNotifiedUseCase: MarkCityAlertNotifiedUseCase = mockk()
    private val notificationService: NotificationService = mockk(relaxed = true)
    private val permissionChecker: PermissionChecker = mockk()

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `when notification permission is not granted, worker returns success`() = runTest {
        // Given
        coEvery { permissionChecker.check(PermissionChecker.Permission.POST_NOTIFICATIONS) } returns false

        val worker = TestListenableWorkerBuilder<TemperatureCheckWorker>(context)
            .setWorkerFactory(object : androidx.work.WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: WorkerParameters
                ): ListenableWorker {
                    return TemperatureCheckWorker(
                        appContext, workerParameters,
                        getAllCitiesUseCase, loadCityWeatherByCoordinatesUseCase,
                        checkTemperatureThresholdUseCase, markCityAlertNotifiedUseCase,
                        notificationService, permissionChecker,
                    )
                }
            })
            .build()

        // When
        val result = worker.doWork()

        // Then
        assertEquals(ListenableWorker.Result.success(), result)
        coVerify(exactly = 0) { getAllCitiesUseCase() }
    }

    @Test
    fun `when temperature exceeds threshold and app is in background, notification is shown`() = runTest {
        // Given
        val city = CityWeatherDomain(name = "Hot City", temperature = 35.0, justAdded = true, pressure = 1013, temperatureMin = 30.0, temperatureMax = 40.0, latitude = 1.0, longitude = 2.0, isAlertNotified = false)
        coEvery { permissionChecker.check(PermissionChecker.Permission.POST_NOTIFICATIONS) } returns true
        every { getAllCitiesUseCase() } returns flowOf(listOf(city))
        every { checkTemperatureThresholdUseCase() } returns flowOf(TemperatureAlertResult(cityName = "Hot City", showAlert = true, isPersistentAlertActive = true, currentTemperature = 35.0, threshold = 30.0))
        coEvery { markCityAlertNotifiedUseCase("Hot City", true) } returns Unit

        val worker = TestListenableWorkerBuilder<TemperatureCheckWorker>(context)
            .setWorkerFactory(object : androidx.work.WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: WorkerParameters
                ): ListenableWorker {
                    return TemperatureCheckWorker(
                        appContext, workerParameters,
                        getAllCitiesUseCase, loadCityWeatherByCoordinatesUseCase,
                        checkTemperatureThresholdUseCase, markCityAlertNotifiedUseCase,
                        notificationService, permissionChecker,
                    )
                }
            })
            .build()

        // When
        val result = worker.doWork()

        // Then
        assertEquals(ListenableWorker.Result.success(), result)
        coVerify { notificationService.showHighTemperatureNotification(35.0) }
        coVerify { markCityAlertNotifiedUseCase("Hot City", true) }
    }

}
