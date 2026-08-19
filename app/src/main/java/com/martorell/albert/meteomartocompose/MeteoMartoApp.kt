package com.martorell.albert.meteomartocompose

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.martorell.albert.meteomartocompose.framework.worker.WorkScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MeteoMartoApp : Application(), Configuration.Provider {

    @Inject
    internal lateinit var workerFactory: HiltWorkerFactory

    @Inject
    internal lateinit var workScheduler: WorkScheduler

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        workScheduler.schedulePeriodicTemperatureCheck()
    }
}
