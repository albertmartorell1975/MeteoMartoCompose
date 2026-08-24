package com.martorell.albert.meteomartocompose.framework.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.martorell.albert.meteomartocompose.data.auth.repositories.auth.AuthRepository
import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepository
import com.martorell.albert.meteomartocompose.di.ApplicationScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [WorkScheduler] is a specialized infrastructure component responsible for orchestrating background tasks.
 * Following Clean Architecture and Reactive patterns, it observes the application state to ensure
 * synchronization between User Session (Auth) and Background Execution (WorkManager).
 */
@Singleton
class WorkScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val authRepository: AuthRepository,
    @param:ApplicationScope private val scope: CoroutineScope,
) {

    companion object {
        private const val TAG = "MeteoMartoDebug"
    }

    /**
     * Entry point to initiate the reactive observation of the background check.
     * It manages the lifecycle of the Worker based on the user's authentication status (SSOT).
     */
    fun schedulePeriodicTemperatureCheck() {
        Log.d(TAG, "[WorkScheduler] Initializing monitoring...")
        scope.launch {
            // Observe the authentication state flow. 
            // This ensures that background work ONLY happens during an active session.
            authRepository.isUserLoggedIn
                .distinctUntilChanged()
                .collectLatest { isLoggedIn ->
                    Log.d(TAG, "[WorkScheduler] Auth state changed: isLoggedIn=$isLoggedIn")
                    if (isLoggedIn) {
                        // User is logged in: Start dynamic interval observation
                        observeIntervalAndSchedule()
                        // Start threshold observation for reactive approach
                        observeThresholdAndTrigger()
                    } else {
                        // User logged out: Tear down all background activities to preserve privacy and battery
                        cancelAllWork()
                    }
                }
        }
    }

    /**
     * Subscribes to Remote Config updates for the weather check interval.
     * When the interval changes in the cloud, the WorkManager task is automatically updated (Resiliency).
     */
    private fun observeIntervalAndSchedule() {
        scope.launch {
            remoteConfigRepository.getWeatherCheckInterval()
                .distinctUntilChanged()
                .collect { intervalMinutes ->
                    Log.d(TAG, "[WorkScheduler] Interval update: $intervalMinutes min")
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()

                    // PeriodicWorkRequestBuilder ensures that the check is repeated over time.
                    val workRequest = PeriodicWorkRequestBuilder<TemperatureCheckWorker>(
                        intervalMinutes, TimeUnit.MINUTES,
                    ).setConstraints(constraints)
                        .build()

                    // UPDATE policy allows refreshing the interval without losing current progress (Navigation 3 pattern).
                    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                        TemperatureCheckWorker.WORK_NAME,
                        ExistingPeriodicWorkPolicy.UPDATE,
                        workRequest,
                    )
                }
        }
    }

    /**
     * Observes the temperature threshold and triggers an immediate check when it changes on Firebase
     * Ideal for testing reactive notifications from Firebase.
     */
    private fun observeThresholdAndTrigger() {
        Log.d(TAG, "[WorkScheduler] Subscribing to Threshold changes...")
        scope.launch {
            remoteConfigRepository.getTemperatureThreshold()
                .collect { threshold ->
                    Log.d(TAG, "[WorkScheduler] THRESHOLD EMITTED from Flow: $threshold. Triggering Worker.")
                    // Trigger an EXPEDITED check. This tells Android to ignore standard background delays.
                    val request = OneTimeWorkRequestBuilder<TemperatureCheckWorker>()
                        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                        .build()
                    WorkManager.getInstance(context).enqueue(request)
                }
        }
    }

    /**
     * Explicitly cancels the unique background task.
     * Used when the domain state changes to 'Not Authenticated'.
     */
    fun cancelAllWork() {
        WorkManager.getInstance(context).cancelUniqueWork(TemperatureCheckWorker.WORK_NAME)
    }
}
