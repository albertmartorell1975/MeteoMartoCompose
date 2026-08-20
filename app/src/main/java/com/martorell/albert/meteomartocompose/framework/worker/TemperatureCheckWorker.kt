package com.martorell.albert.meteomartocompose.framework.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.PermissionChecker
import com.martorell.albert.meteomartocompose.data.notification.NotificationService
import com.martorell.albert.meteomartocompose.usecases.cityweather.CheckTemperatureThresholdUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.GetAllCitiesUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.LoadCityWeatherByCoordinatesUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.MarkCityAlertNotifiedUseCase
import com.martorell.albert.meteomartocompose.utils.AppLifecycleObserver
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class TemperatureCheckWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val getAllCitiesUseCase: GetAllCitiesUseCase,
    private val loadCityWeatherByCoordinatesUseCase: LoadCityWeatherByCoordinatesUseCase,
    private val checkTemperatureThresholdUseCase: CheckTemperatureThresholdUseCase,
    private val markCityAlertNotifiedUseCase: MarkCityAlertNotifiedUseCase,
    private val notificationService: NotificationService,
    private val permissionChecker: PermissionChecker,
    private val appLifecycleObserver: AppLifecycleObserver,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // 1. Check if we have notification permission (Android 13+)
            if (!permissionChecker.check(PermissionChecker.Permission.POST_NOTIFICATIONS)) {
                return Result.success()
            }

            // 2. Get the city currently being monitored (justAdded = true)
            val cities = getAllCitiesUseCase().firstOrNull() ?: emptyList()
            val currentCity = cities.find { it.justAdded }
                ?: return Result.success()

            // 3. Force refresh weather from network to have latest data
            loadCityWeatherByCoordinatesUseCase(
                latitude = currentCity.latitude.toString(),
                longitude = currentCity.longitude.toString(),
            )

            // 4. Check if the new temperature exceeds the threshold and state transitions
            val alertResult = checkTemperatureThresholdUseCase().firstOrNull()

            // 5. Trigger notification if conditions are met
            alertResult?.let {
                if (it.showAlert) {
                    // Check if app is in background to show system notification
                    if (!appLifecycleObserver.isAppInForeground()) {
                        notificationService.showHighTemperatureNotification(it.currentTemperature)
                        // Mark as notified in DB to avoid spam
                        markCityAlertNotifiedUseCase(cityName = it.cityName, notified = true)
                    }
                } else if (!it.isPersistentAlertActive) {
                    // Reset notified status if temperature is back to normal
                    markCityAlertNotifiedUseCase(cityName = it.cityName, notified = false)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error in doWork", e)
            Result.retry()
        }
    }

    companion object {
        private const val TAG = "TemperatureCheckWorker"
        const val WORK_NAME = "TemperatureCheckWork"
    }
}
