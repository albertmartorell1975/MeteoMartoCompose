package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTemperatureThresholdUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) {
    operator fun invoke(): Flow<Double> = remoteConfigRepository.getTemperatureThreshold()
}
