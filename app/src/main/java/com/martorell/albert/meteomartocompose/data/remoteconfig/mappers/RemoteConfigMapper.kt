package com.martorell.albert.meteomartocompose.data.remoteconfig.mappers

class RemoteConfigMapper(
    private val defaultThreshold: Double,
    private val minLimit: Double,
    private val maxLimit: Double
) {

    fun mapToThreshold(value: Double): Double {
        return if (value <= minLimit || value >= maxLimit) {
            defaultThreshold
        } else {
            value
        }
    }
}