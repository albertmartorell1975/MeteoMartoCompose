package com.martorell.albert.meteomartocompose.data.remoteconfig

import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepository
import com.martorell.albert.meteomartocompose.data.remoteconfig.sources.RemoteConfigDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteConfigRepositoryImpl @Inject constructor(
    private val remoteConfigDataSource: RemoteConfigDataSource
) : RemoteConfigRepository {
    override fun getTemperatureThreshold(): Flow<Double> {
        return remoteConfigDataSource.getTemperatureThreshold()
    }
}
