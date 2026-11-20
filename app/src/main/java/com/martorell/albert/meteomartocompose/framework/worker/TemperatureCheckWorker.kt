package com.martorell.albert.meteomartocompose.framework.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.martorell.albert.meteomartocompose.utils.AppConstants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TemperatureCheckWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val temperatureCheckCoordinator: TemperatureCheckCoordinator,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "TemperatureCheckWorker started")
        return when (temperatureCheckCoordinator.execute()) {
            TemperatureCheckOutcome.COMPLETED -> Result.success()
            TemperatureCheckOutcome.RETRY -> {
                Log.e(TAG, "TemperatureCheckWorker encountered an error and will retry")
                Result.retry()
            }
        }
    }

    companion object {
        private const val TAG = AppConstants.DEBUG_TAG
        const val WORK_NAME = "TemperatureCheckWork"
    }
}
