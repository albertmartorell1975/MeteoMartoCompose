package com.martorell.albert.meteomartocompose.data.remoteconfig.sources

import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface RemoteConfigDataSource {
    fun getTemperatureThreshold(): Flow<Result<Double, DomainError>>
    fun getWeatherCheckInterval(): Flow<Result<Long, DomainError>>
}
