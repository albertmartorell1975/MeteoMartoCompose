package com.martorell.albert.meteomartocompose.domain.cityweather

/**
 * Semantic weather classification derived from raw OpenWeather condition identifiers.
 *
 * Used by domain models and downstream presentation layers to map meteorological telemetry
 * into discrete categories without depending on raw API condition codes.
 */
enum class WeatherCondition {
    /** Default fallback state for cloudy, atmospheric, or unclassified conditions. */
    BASELINE,

    /** Clear sky condition corresponding to OpenWeather code `800`. */
    SUNNY,

    /** Precipitation conditions covering thunderstorm, drizzle, rain, and snow ranges. */
    RAIN,
    ;

    companion object {
        private const val CLEAR_SKY_ID = 800
        private val THUNDERSTORM_RANGE = 200..232
        private val DRIZZLE_RANGE = 300..321
        private val RAIN_RANGE = 500..531
        private val SNOW_RANGE = 600..622

        /**
         * Maps an OpenWeather condition identifier to its corresponding [WeatherCondition].
         *
         * Clear sky (`800`) resolves to [SUNNY]; thunderstorm (`200..232`), drizzle (`300..321`),
         * rain (`500..531`), and snow (`600..622`) resolve to [RAIN]; all other codes
         * (including atmosphere, clouds, and unknown values) fall back to [BASELINE].
         *
         * @param conditionId Raw OpenWeather `weather[0].id` numeric condition code.
         * @return Deterministic [WeatherCondition] category for the given [conditionId].
         */
        fun fromOpenWeatherId(conditionId: Int): WeatherCondition =
            when (conditionId) {
                CLEAR_SKY_ID -> SUNNY
                in THUNDERSTORM_RANGE,
                in DRIZZLE_RANGE,
                in RAIN_RANGE,
                in SNOW_RANGE,
                -> RAIN
                else -> BASELINE
            }
    }
}
