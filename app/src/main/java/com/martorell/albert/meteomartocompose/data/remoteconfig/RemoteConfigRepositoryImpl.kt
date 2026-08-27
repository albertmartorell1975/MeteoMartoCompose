package com.martorell.albert.meteomartocompose.data.remoteconfig

import com.martorell.albert.meteomartocompose.data.remoteconfig.mappers.RemoteConfigMapper
import com.martorell.albert.meteomartocompose.data.remoteconfig.sources.RemoteConfigDataSource
import com.martorell.albert.meteomartocompose.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigRepositoryImpl @Inject constructor(
    private val remoteConfigDataSource: RemoteConfigDataSource,
    private val mapper: RemoteConfigMapper,
    @param:ApplicationScope private val scope: CoroutineScope
) : RemoteConfigRepository {

    // We use 'Eagerly' so the Firebase listener never closes,
    // even if the Scheduler is suspended for a moment in the background.
    private val thresholdFlow = remoteConfigDataSource.getTemperatureThreshold()
        .map { result -> mapper.mapToThreshold(result.getOrNull()) }
        .shareIn(scope, SharingStarted.Eagerly, replay = 1)

    override fun getTemperatureThreshold(): Flow<Double> = thresholdFlow

    override fun getWeatherCheckInterval(): Flow<Long> {
        return remoteConfigDataSource.getWeatherCheckInterval().map { result ->
            mapper.mapToInterval(result.getOrNull())
        }
    }
}
