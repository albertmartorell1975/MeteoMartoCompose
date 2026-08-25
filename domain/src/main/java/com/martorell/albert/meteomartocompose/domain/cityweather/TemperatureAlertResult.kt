package com.martorell.albert.meteomartocompose.domain.cityweather

data class TemperatureAlertResult(
    val cityName: String,
    val showAlert: Boolean,
    val isPersistentAlertActive: Boolean,
    val currentTemperature: Double,
    val threshold: Double,
) {
    companion object {
        val EMPTY = TemperatureAlertResult(
            cityName = "",
            showAlert = false,
            isPersistentAlertActive = false,
            currentTemperature = 0.0,
            threshold = 0.0
        )
    }
}
