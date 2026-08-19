package com.martorell.albert.meteomartocompose.framework.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepository
import com.martorell.albert.meteomartocompose.di.ApplicationScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteConfigRepository: RemoteConfigRepository,
    @ApplicationScope private val scope: CoroutineScope
) {
    fun schedulePeriodicTemperatureCheck() {
        scope.launch {
            remoteConfigRepository.getWeatherCheckInterval()
                .distinctUntilChanged()
                .collect { intervalMinutes ->
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()

                    val workRequest = PeriodicWorkRequestBuilder<TemperatureCheckWorker>(
                        intervalMinutes, TimeUnit.MINUTES
                    ).setConstraints(constraints)
                        .build()

                    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                        "TemperatureCheckWork",
                        ExistingPeriodicWorkPolicy.UPDATE,
                        workRequest
                    )
                }
        }
    }
}
