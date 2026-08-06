package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTemperatureThresholdUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) {
    fun invoke(): Flow<Double> = remoteConfigRepository.getTemperatureThreshold()
}
