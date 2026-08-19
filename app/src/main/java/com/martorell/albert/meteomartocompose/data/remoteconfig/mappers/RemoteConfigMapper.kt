package com.martorell.albert.meteomartocompose.data.remoteconfig.mappers

class RemoteConfigMapper(
    private val defaultThreshold: Double,
    private val minLimit: Double,
    private val maxLimit: Double,
    private val defaultInterval: Long,
    private val minInterval: Long
) {

    fun mapToThreshold(value: Double?): Double {
        if (value == null) return defaultThreshold
        return if (value <= minLimit || value >= maxLimit) {
            defaultThreshold
        } else {
            value
        }
    }

    fun mapToInterval(value: Long?): Long {
        if (value == null) return defaultInterval
        return if (value < minInterval) {
            defaultInterval
        } else {
            value
        }
    }

    companion object {
        const val DEFAULT_THRESHOLD = 30.0
        const val MIN_TEMP_LIMIT = -100.0
        const val MAX_TEMP_LIMIT = 100.0
        const val DEFAULT_INTERVAL = 60L
        const val MIN_INTERVAL = 15L
    }
}
