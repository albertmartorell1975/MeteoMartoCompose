package com.martorell.albert.meteomartocompose.data.remoteconfig

import kotlinx.coroutines.flow.Flow

interface RemoteConfigRepository {
    fun getTemperatureThreshold(): Flow<Double>
    fun getWeatherCheckInterval(): Flow<Long>
}
