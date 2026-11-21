package com.martorell.albert.meteomartocompose.framework.worker

import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionChecker
import com.martorell.albert.meteomartocompose.data.notification.NotificationService
import com.martorell.albert.meteomartocompose.usecases.cityweather.CheckTemperatureThresholdUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.GetAllCitiesUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.LoadCityWeatherByCoordinatesUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.MarkCityAlertNotifiedUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

/**
 * Outcome returned by [TemperatureCheckCoordinator.execute] to indicate how the
 * scheduling worker should finish its execution.
 */
enum class TemperatureCheckOutcome {
    /**
     * Execution finished cleanly or skipped work intentionally because prerequisites
     * (such as notification permission or a monitored city) were not met.
     */
    COMPLETED,

    /**
     * Execution encountered a recoverable failure and should be retried by the scheduler.
     */
    RETRY,
}

/**
 * Pure Kotlin orchestrator for background temperature threshold checks.
 *
 * Coordinates notification permission verification, active city lookup, weather refresh,
 * threshold evaluation, and notification/alert state updates without Android framework
 * dependencies so the flow can be tested on the pure JVM.
 */
class TemperatureCheckCoordinator @Inject constructor(
    private val getAllCitiesUseCase: GetAllCitiesUseCase,
    private val loadCityWeatherByCoordinatesUseCase: LoadCityWeatherByCoordinatesUseCase,
    private val checkTemperatureThresholdUseCase: CheckTemperatureThresholdUseCase,
    private val markCityAlertNotifiedUseCase: MarkCityAlertNotifiedUseCase,
    private val notificationService: NotificationService,
    private val permissionChecker: PermissionChecker,
) {

    /**
     * Runs a single temperature-check cycle for the currently monitored city.
     *
     * Rethrows [CancellationException] to preserve structured concurrency, and maps
     * any other unexpected [Exception] to [TemperatureCheckOutcome.RETRY].
     *
     * @return [TemperatureCheckOutcome.COMPLETED] when finished or intentionally skipped,
     * or [TemperatureCheckOutcome.RETRY] when a non-cancellation error occurs.
     */
    suspend fun execute(): TemperatureCheckOutcome {
        return try {
            if (!permissionChecker.check(PermissionChecker.Permission.POST_NOTIFICATIONS)) {
                return TemperatureCheckOutcome.COMPLETED
            }

            val cities = getAllCitiesUseCase().firstOrNull().orEmpty()
            val currentCity = cities.find { it.justAdded }
                ?: return TemperatureCheckOutcome.COMPLETED

            loadCityWeatherByCoordinatesUseCase(
                latitude = currentCity.latitude.toString(),
                longitude = currentCity.longitude.toString(),
            )

            val alertResult = checkTemperatureThresholdUseCase().firstOrNull()
            alertResult?.let {
                if (it.showAlert) {
                    notificationService.showHighTemperatureNotification(it.currentTemperature)
                    markCityAlertNotifiedUseCase(cityName = it.cityName, notified = true)
                } else if (!it.isPersistentAlertActive) {
                    markCityAlertNotifiedUseCase(cityName = it.cityName, notified = false)
                }
            }

            TemperatureCheckOutcome.COMPLETED
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            TemperatureCheckOutcome.RETRY
        }
    }
}
