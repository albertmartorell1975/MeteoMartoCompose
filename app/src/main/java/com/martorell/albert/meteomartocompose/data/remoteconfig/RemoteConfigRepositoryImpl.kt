package com.martorell.albert.meteomartocompose.data.remoteconfig

import com.martorell.albert.meteomartocompose.data.remoteconfig.mappers.RemoteConfigMapper
import com.martorell.albert.meteomartocompose.data.remoteconfig.sources.RemoteConfigDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteConfigRepositoryImpl @Inject constructor(
    private val remoteConfigDataSource: RemoteConfigDataSource,
    private val mapper: RemoteConfigMapper,
) : RemoteConfigRepository {
    override fun getTemperatureThreshold(): Flow<Double> {
        return remoteConfigDataSource.getTemperatureThreshold().map {
            mapper.mapToThreshold(it)
        }
    }
}
