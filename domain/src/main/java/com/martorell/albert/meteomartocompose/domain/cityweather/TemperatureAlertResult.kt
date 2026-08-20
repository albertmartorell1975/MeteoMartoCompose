package com.martorell.albert.meteomartocompose.domain.cityweather

data class TemperatureAlertResult(
    val cityName: String,
    val showAlert: Boolean,
    val isPersistentAlertActive: Boolean,
    val currentTemperature: Double,
    val threshold: Double,
)
