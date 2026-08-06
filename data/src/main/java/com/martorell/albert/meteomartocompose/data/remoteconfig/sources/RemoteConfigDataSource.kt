package com.martorell.albert.meteomartocompose.data.remoteconfig.sources

import kotlinx.coroutines.flow.Flow

interface RemoteConfigDataSource {
    fun getTemperatureThreshold(): Flow<Double>
}
