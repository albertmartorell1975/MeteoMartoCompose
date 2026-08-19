package com.martorell.albert.meteomartocompose.data.remoteconfig.sources

import com.martorell.albert.meteomartocompose.data.ResultResponse
import kotlinx.coroutines.flow.Flow

interface RemoteConfigDataSource {
    fun getTemperatureThreshold(): Flow<ResultResponse<Double>>
    fun getWeatherCheckInterval(): Flow<ResultResponse<Long>>
}
